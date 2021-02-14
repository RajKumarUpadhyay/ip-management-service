package go.ip.services;

import go.ip.common.IPAddressReserveRequest;
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
     *
     * @param ipAddressReserveRequest
     * @return
     * @throws AddressStringException
     */
    public List<String> generateAndReserveIPAddress(IPAddressReserveRequest ipAddressReserveRequest) throws AddressStringException {

        logger.info("Fetch pool information based on pool id...");
        Optional<IPPool> ipPool = ipPoolRepositiory.findById(ipAddressReserveRequest.getIpPoolId());
        logger.info("Fetch reserved IP Address information based on IP Address table based on pool id if exist..");
        Optional<List<IPAddress>> reservedIPAddress = ipAddressRepository.findAllByipPoolIdOrderByValueDesc(ipAddressReserveRequest.getIpPoolId());

        logger.info("validateIPAddressReservationRequest() method has been invoked for validation pool and request...");
        validateIPAddressReservationRequest(ipPool, ipAddressReserveRequest);

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
        List<String> requestedNoOfIpAddress = ipManagementServiceUtils.getRequestedNoOfIpFromGivenRange(lowerBound, ipPool.get().getUpperBound(), ipAddressReserveRequest.getNoOfIpAddress().get());
        logger.info("persist all genegrate ip into IP Address table with pool id as a reference...");
        requestedNoOfIpAddress.forEach(ip -> {
            IPAddress ipAddress = new IPAddress();
            ipAddress.setIpPoolId(ipAddressReserveRequest.getIpPoolId());
            ipAddress.setResourceState(ResourceState.RESERVED.name());
            ipAddress.setValue(ip);
            ipAddressRepository.save(ipAddress);
        });

        logger.info("Update capacity of pool..");
        ipPool.get().setUsedCapacity(ipPool.get().getUsedCapacity() + ipAddressReserveRequest.getNoOfIpAddress().get());

        ipPoolRepositiory.save(ipPool.get());

        return requestedNoOfIpAddress;
    }

    /**
     *
     * @param ipAddressReserveRequest
     * @return
     * @throws AddressStringException
     */
    public String reserveIPAddress(IPAddressReserveRequest ipAddressReserveRequest) throws AddressStringException {
        Optional<IPPool> ipPool = ipPoolRepositiory.findById(ipAddressReserveRequest.getIpPoolId());

        validateIPAddressReservationRequest(ipPool, ipAddressReserveRequest);

        Optional<IPAddress> ipAddress = ipAddressRepository.findByValue(ipAddressReserveRequest.getIpAddress().get());

        if(ipAddress.isPresent()) {
            if (ipAddress.get().getResourceState().equalsIgnoreCase(String.valueOf(ResourceState.RESERVED)))
                throw new IPReservationFailedException("Requested IP: "+ipAddress.get().getValue()+ " has been RESERVED!");
            else if (ipAddress.get().getResourceState().equalsIgnoreCase(String.valueOf(ResourceState.BLACKLISTED)))
                throw new IPReservationFailedException("Requested IP: "+ipAddress.get().getValue()+ " has been BLACKLISTED!");
        }
        ipAddress.get().setResourceState(ResourceState.RESERVED.name());

        ipAddressRepository.save(ipAddress.get());

        return ipAddress.get().getValue();
    }

    /**
     *
     * @param ipPool
     * @param ipAddressReserveRequest
     */
    private final void validateIPAddressReservationRequest(Optional<IPPool> ipPool, IPAddressReserveRequest ipAddressReserveRequest) {
        if(!ipPool.isPresent())
            throw new ResourceNotFoundException("Requested Pool ID Not Present!");

        if(Objects.nonNull(ipAddressReserveRequest.getNoOfIpAddress())) {
            ipPool.ifPresent(pool -> {
                if (pool.getTotalCapacity() < ipAddressReserveRequest.getNoOfIpAddress().get()) {
                    throw new IPCapacityOverflowException("Requested No Of IP is greater than available resource!");
                } else if (pool.getTotalCapacity() == pool.getUsedCapacity()) {
                    throw new IPCapacityOverflowException("Pool capacity has been overflow. No reservation possible for this pool!");
                }
            });
        }

        if (Objects.nonNull(ipAddressReserveRequest.getIpAddress())) {
            if (!ipManagementServiceUtils.isIPAddressInRange(ipPool.get().getLowerBound(), ipPool.get().getUpperBound(), ipAddressReserveRequest.getIpAddress().get()))
                throw new IPReservationFailedException("Provided IP doesn't exist in the given pool id range!");
        }
    }
}
