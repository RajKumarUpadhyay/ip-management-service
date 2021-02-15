package go.ip.services;

import go.ip.common.IPAddressRequest;
import go.ip.common.IPAddressResponse;
import go.ip.common.ResourceState;
import go.ip.entities.IPAddress;
import go.ip.entities.IPPool;
import go.ip.exception.IPCapacityOverflowException;
import go.ip.exception.IPReservationFailedException;
import go.ip.exception.ResourceNotFoundException;
import go.ip.repoistories.IPAddressRepository;
import go.ip.repoistories.IPPoolRepositiory;
import go.ip.utils.IPManagementServiceUtils;
import inet.ipaddr.AddressStringException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class IpManagementService {

    public static final Logger logger = Logger.getLogger(IpManagementService.class);

    @Autowired
    private IPAddressRepository ipAddressRepository;
    @Autowired
    private IPPoolRepositiory ipPoolRepositiory;
    @Autowired
    IPManagementServiceUtils ipManagementServiceUtils;

    /**
     * This method has been designed to generate and reserve the IP into IP Address table. This method will generate
     * requested no. of IP from specified pool id in request and return as list of IP address as response.
     *
     * @param ipAddressRequest
     * @return
     * @throws AddressStringException
     */
    public List<String> generateAndReserveIPAddress(IPAddressRequest ipAddressRequest) throws AddressStringException {

        logger.info("Fetch pool information based on pool id...");
        Optional<IPPool> ipPool = ipPoolRepositiory.findById(ipAddressRequest.getIpPoolId());
        logger.info("Fetch reserved IP Address information based on IP Address table based on pool id if exist..");
        Optional<List<IPAddress>> reservedIPAddress = ipAddressRepository.findAllByipPoolIdOrderByValueDesc(ipAddressRequest.getIpPoolId());

        logger.info("validateIPAddressReservationRequest() method has been invoked for validation pool and request...");
        validateIPAddressReservationRequest(ipPool, ipAddressRequest);

        logger.info("Sort reserved IP Address for generate next sequence of IP Address from Pool...");
        if (reservedIPAddress.isPresent() && reservedIPAddress.get().size() > 0) {
            Collections.sort(reservedIPAddress.get(), new Comparator<IPAddress>() {
                @Override
                public int compare(IPAddress ipAddress, IPAddress ipAddress2) {

                    String[] ip1 = ipAddress.getValue().split("\\.");
                    String ipFormatted1 = String.format("%3s.%3s.%3s.%3s", ip1[0],ip1[1],ip1[2],ip1[3]);
                    String[] ip2 = ipAddress2.getValue().split("\\.");
                    String ipFormatted2 = String.format("%3s.%3s.%3s.%3s",  ip2[0],ip2[1],ip2[2],ip2[3]);
                    return ipFormatted1.compareTo(ipFormatted2);
                }
            });
        }

        String lowerBound = reservedIPAddress.isPresent() && reservedIPAddress.get().size() > 0 ? reservedIPAddress.get().get(reservedIPAddress.get().size() -1).getValue(): ipPool.get().getLowerBound();
        logger.info("lowerBound for fetch next available IP range with defined pool");
        List<String> requestedNoOfIpAddress = ipManagementServiceUtils.getRequestedNoOfIpFromGivenRange(lowerBound, ipPool.get().getUpperBound(), ipAddressRequest.getNoOfIpAddress().get());
        logger.info("persist all generated ip into the IP Address table with pool id as a reference...");
        requestedNoOfIpAddress.forEach(ip -> {
            IPAddress ipAddress = new IPAddress();
            ipAddress.setIpPoolId(ipAddressRequest.getIpPoolId());
            ipAddress.setResourceState(ResourceState.RESERVED.name());
            ipAddress.setValue(ip);
            ipAddressRepository.save(ipAddress);
        });

        logger.info("Update capacity of pool..");
        ipPool.get().setUsedCapacity(ipPool.get().getUsedCapacity() + ipAddressRequest.getNoOfIpAddress().get());

        ipPoolRepositiory.save(ipPool.get());

        return requestedNoOfIpAddress;
    }

    /**
     * this method will reserve the IP address if request IP address has not been blacklisted or reserved in the POOL and
     * validate the IP range from pool if the ip within range. If above specified condition has not been matched then it
     * throw error response to the client.
     *
     * @param ipAddressRequest
     * @return
     * @throws AddressStringException
     */
    public String reserveIPAddress(IPAddressRequest ipAddressRequest) throws AddressStringException {
        logger.info("reserveIPAddress() has been invoked");
        Optional<IPAddress> ipAddress = processAllTypeOfIPAddressRequest(ipAddressRequest);
        if(ipAddress.isPresent()) {
            if (ipAddress.get().getResourceState().equalsIgnoreCase(String.valueOf(ResourceState.RESERVED)))
                throw new IPReservationFailedException("Requested IP: "+ipAddress.get().getValue()+ " has been RESERVED!");
            else if (ipAddress.get().getResourceState().equalsIgnoreCase(String.valueOf(ResourceState.BLACKLISTED)))
                throw new IPReservationFailedException("Requested IP: "+ipAddress.get().getValue()+ " has been BLACKLISTED!");
        }
        logger.info("Reserved IP ..."+ipAddress.get().getValue());
        ipAddress.get().setResourceState(ResourceState.RESERVED.name());

        ipAddressRepository.save(ipAddress.get());
        logger.info("IP Address has been reserved ..."+ipAddress.get().getValue());
        return ipAddress.get().getValue();
    }

    /**
     * This method will blacklist given IP Address. This will blacklist only non-reserved IP Could be blacklist.
     * @param ipAddressRequest
     * @return
     */
    public String blacklistIPAddress(IPAddressRequest ipAddressRequest) {
        Optional<IPAddress> ipAddress = processAllTypeOfIPAddressRequest(ipAddressRequest);
        if(ipAddress.isPresent()) {
            if (ipAddress.get().getResourceState().equalsIgnoreCase(String.valueOf(ResourceState.RESERVED)))
                throw new IPReservationFailedException("Requested IP Address: "+ipAddress.get().getValue()+ "  for blacklisting has RESERVED state! Can't blacklist the IP until it's the state has not been changed.");
            else if (ipAddress.get().getResourceState().equalsIgnoreCase(String.valueOf(ResourceState.BLACKLISTED)))
                throw new IPReservationFailedException("Requested IP Address: "+ipAddress.get().getValue()+ " already BLACKLISTED!");
        }
        logger.info("Reserved IP ..."+ipAddress.get().getValue());
        ipAddress.get().setResourceState(ResourceState.BLACKLISTED.name());

        ipAddressRepository.save(ipAddress.get());
        logger.info("IP Address has been blacklisted ..."+ipAddress.get().getValue());
        return ipAddress.get().getValue();
    }

    /**
     * This method will free IP address from IP Pool if IP address in RESERVED state. BLACKLISTED IP can't be free.
     * @param ipAddressRequest
     * @return
     */
    public String freeIPAddress(IPAddressRequest ipAddressRequest) {
        Optional<IPAddress> ipAddress = processAllTypeOfIPAddressRequest(ipAddressRequest);
        if(ipAddress.isPresent()) {
            if (ipAddress.get().getResourceState().equalsIgnoreCase(String.valueOf(ResourceState.BLACKLISTED)))
                throw new IPReservationFailedException("Requested IP Address: "+ipAddress.get().getValue()+ "  is blacklisted. Can't make it available util state of the IP has not been changed.");
        }
        logger.info("Reserved IP ..."+ipAddress.get().getValue());
        ipAddress.get().setResourceState(ResourceState.FREE.name());

        ipAddressRepository.save(ipAddress.get());
        logger.info("IP Address has been free for allocation ..."+ipAddress.get().getValue());
        return ipAddress.get().getValue();
    }

    /**
     * This method will return info of IP Address.
     * @param ipAddressRequest
     * @return
     */
    public IPAddressResponse getIpInfo(IPAddressRequest ipAddressRequest) {
        IPAddressResponse ipAddressResponse = new IPAddressResponse();
        Optional<IPPool> ipPool = ipPoolRepositiory.findById(ipAddressRequest.getIpPoolId());

        validateIPAddressReservationRequest(ipPool, ipAddressRequest);
        logger.info("find IP Address in table if exist...");
        Optional<IPAddress> ipAddress = ipAddressRepository.findByValue(ipAddressRequest.getIpAddress().get());
        ipAddressResponse.setIpPool(ipPool.get());
        ipAddressResponse.setIpAddress(ipAddress.get());

        return ipAddressResponse;
    }

    /**
     * This method will
     * @param ipAddressRequest
     * @return
     */
    private Optional<IPAddress> processAllTypeOfIPAddressRequest(IPAddressRequest ipAddressRequest) {
        Optional<IPPool> ipPool = ipPoolRepositiory.findById(ipAddressRequest.getIpPoolId());

        validateIPAddressReservationRequest(ipPool, ipAddressRequest);
        logger.info("find IP Address in table if exist...");
        Optional<IPAddress> ipAddress = ipAddressRepository.findByValue(ipAddressRequest.getIpAddress().get());
        return ipAddress;
    }
    /**
     * This method will validate ip address reservation request based on multiple criteria.
     * @param ipPool
     * @param ipAddressRequest
     */
    private final void validateIPAddressReservationRequest(Optional<IPPool> ipPool, IPAddressRequest ipAddressRequest) {
        if(!ipPool.isPresent())
            throw new ResourceNotFoundException("Requested Pool ID Not Present!");

        if(Objects.nonNull(ipAddressRequest.getNoOfIpAddress())) {
            ipPool.ifPresent(pool -> {
                if (pool.getTotalCapacity() < ipAddressRequest.getNoOfIpAddress().get()) {
                    throw new IPCapacityOverflowException("Requested No Of IP is greater than available resource!");
                } else if (pool.getTotalCapacity() <= pool.getUsedCapacity()) {
                    throw new IPCapacityOverflowException("Pool capacity has been overflow. No reservation possible for this pool!");
                }
            });
        }

        if (Objects.nonNull(ipAddressRequest.getIpAddress())) {
            if (!ipManagementServiceUtils.isIPAddressInRange(ipPool.get().getLowerBound(), ipPool.get().getUpperBound(), ipAddressRequest.getIpAddress().get()))
                throw new IPReservationFailedException("Provided IP doesn't exist in the given pool id range!");
        }
    }
}
