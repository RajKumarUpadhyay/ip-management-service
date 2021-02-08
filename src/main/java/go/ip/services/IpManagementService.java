package go.ip.services;

import go.ip.common.GenerateAndReserveRequest;
import go.ip.entities.IPAddress;
import go.ip.entities.IPPool;
import go.ip.exception.ResourceNotFoundException;
import go.ip.repoistories.IPAddressRepository;
import go.ip.repoistories.IPPoolRepositiory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IpManagementService {

    @Autowired
    private IPAddressRepository ipAddressRepository;
    @Autowired
    private IPPoolRepositiory ipPoolRepositiory;

    public IPAddress reserveIpAddress(GenerateAndReserveRequest generateAndReserveRequest) {
        validateIpReservationRequest(generateAndReserveRequest);


        return new IPAddress();
    }

    private final void validateIpReservationRequest(GenerateAndReserveRequest generateAndReserveRequest) {
        Optional<IPPool> matchingObject = ipPoolRepositiory.findAll().stream().
                filter(p -> p.getDescription().equals(generateAndReserveRequest.getIpPoolId())).
                findFirst();
        if (!matchingObject.isPresent())
            throw new ResourceNotFoundException("Provided IpPoolId "+ generateAndReserveRequest.getIpPoolId() + " Not found!");
        if (matchingObject.get().getTotalCapacity() < generateAndReserveRequest.getNoOfIpAddress())
            throw new RuntimeException("Request No of IP address is greater than the pool size!");
    }

}
