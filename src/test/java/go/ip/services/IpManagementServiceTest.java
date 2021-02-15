package go.ip.services;

import go.ip.common.IPAddressRequest;
import go.ip.common.IPAddressResponse;
import go.ip.common.ResourceState;
import go.ip.entities.IPAddress;
import go.ip.repoistories.IPAddressRepository;
import inet.ipaddr.AddressStringException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IpManagementServiceTest {

    @Autowired
    IpManagementService ipManagementService;

    @Autowired
    IPAddressRepository ipAddressRepository;

    @Test
    void generateAndReserveIPAddress() throws AddressStringException {
        IPAddressRequest ipAddressRequest = new IPAddressRequest();
        ipAddressRequest.setIpPoolId(1);
        ipAddressRequest.setNoOfIpAddress(java.util.Optional.of(10));
        List<String> lisOfIPAddress =  ipManagementService.generateAndReserveIPAddress(ipAddressRequest);

        assertNotNull(lisOfIPAddress);
        assertEquals(10, lisOfIPAddress.size());
    }

    @Test
    void reserveIPAddress() throws AddressStringException {
        IPAddress ipAddress = new IPAddress();
        ipAddress.setValue("10.70.26.4");
        ipAddress.setIpPoolId(1);
        ipAddress.setResourceState(ResourceState.FREE.name());

        ipAddressRepository.save(ipAddress);

        IPAddressRequest ipAddressRequest = new IPAddressRequest();
        ipAddressRequest.setIpPoolId(1);
        ipAddressRequest.setIpAddress(java.util.Optional.of("10.70.26.4"));

        String ipAddressReserved = ipManagementService.reserveIPAddress(ipAddressRequest);
        assertNotNull(ipAddressReserved);
        assertEquals("10.70.26.4", ipAddressReserved);
    }

    @Test
    void blacklistIPAddress() {

        Optional<IPAddress> ipAddress = ipAddressRepository.findByValue("10.70.26.4");
        ipAddress.get().setResourceState(ResourceState.FREE.name());
        ipAddressRepository.save(ipAddress.get());
        IPAddressRequest ipAddressRequest = new IPAddressRequest();
        ipAddressRequest.setIpPoolId(1);
        ipAddressRequest.setIpAddress(java.util.Optional.of("10.70.26.4"));

        String ipAddressReserved = ipManagementService.blacklistIPAddress(ipAddressRequest);
        assertNotNull(ipAddressReserved);
        assertEquals("10.70.26.4", ipAddressReserved);
    }

    @Test
    void freeIPAddress() {

        Optional<IPAddress> ipAddress = ipAddressRepository.findByValue("10.70.26.4");
        ipAddress.get().setResourceState(ResourceState.RESERVED.name());
        ipAddressRepository.save(ipAddress.get());

        IPAddressRequest ipAddressRequest = new IPAddressRequest();
        ipAddressRequest.setIpPoolId(1);
        ipAddressRequest.setIpAddress(java.util.Optional.of("10.70.26.4"));

        String ipAddressReserved = ipManagementService.freeIPAddress(ipAddressRequest);
        assertNotNull(ipAddressReserved);
        assertEquals("10.70.26.4", ipAddressReserved);
    }

    @Test
    void getIpInfo() {

        IPAddressRequest ipAddressRequest = new IPAddressRequest();
        ipAddressRequest.setIpPoolId(1);
        ipAddressRequest.setIpAddress(java.util.Optional.of("10.70.26.4"));

        IPAddressResponse ipAddressResponse = ipManagementService.getIpInfo(ipAddressRequest);
        assertNotNull(ipAddressResponse);
    }
}