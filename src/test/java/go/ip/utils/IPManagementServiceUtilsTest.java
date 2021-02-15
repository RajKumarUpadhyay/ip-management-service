package go.ip.utils;

import inet.ipaddr.AddressStringException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IPManagementServiceUtilsTest {

    final static String LOWER_BOUND = "10.70.26.0";
    final static String UPPER_BOUND = "10.70.26.50";
    final static String IS_IP_ADDRESS_IN_RANGE = "10.70.26.2";

    final static int EXPECTED_IP_ADDRESS_COUNT = 50;

    @Autowired
    IPManagementServiceUtils ipManagementServiceUtils;

    @Test
    void calculateCapacity() {
        int availableNoOIpAddress = ipManagementServiceUtils.calculateCapacity(LOWER_BOUND,UPPER_BOUND);
        assertNotNull(availableNoOIpAddress);
        assertEquals(EXPECTED_IP_ADDRESS_COUNT, availableNoOIpAddress);
    }

    @Test
    void isIPAddressInRange() {
      boolean isIPAddressInRange =  ipManagementServiceUtils.isIPAddressInRange(LOWER_BOUND, UPPER_BOUND, IS_IP_ADDRESS_IN_RANGE);
      assertTrue(isIPAddressInRange);
      isIPAddressInRange =  ipManagementServiceUtils.isIPAddressInRange(LOWER_BOUND, UPPER_BOUND, "10.70.26.100");
      assertFalse(isIPAddressInRange);
    }

    @Test
    void getRequestedNoOfIpFromGivenRange() throws AddressStringException {
       List<String> listOfIpAddress = ipManagementServiceUtils.getRequestedNoOfIpFromGivenRange(LOWER_BOUND, UPPER_BOUND, 5);
       assertNotNull(listOfIpAddress);
       assertEquals(5, listOfIpAddress.size());
    }
}