package go.ip.utils;

import inet.ipaddr.AddressStringException;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressSeqRange;
import inet.ipaddr.IPAddressString;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class IPManagementServiceUtils {

    public static final Logger logger = Logger.getLogger(IPManagementServiceUtils.class);

    /**
     * This method will calculate the available no of ip between lower bound & upper bound ip addresses.
     * @param lowerBound
     * @param upperBound
     * @return
     */
    public int calculateCapacity(String lowerBound, String upperBound) {
        logger.info("Capacity calculator has been invoked..");
        int lower_Bound = 0;
        int upper_Bound = 0;

        lower_Bound = calculator(lowerBound);
        upper_Bound = calculator(upperBound);

        return (upper_Bound - lower_Bound);
    }

    /**
     * this method will accept ip address as parameter and convert into octet.
     * @param ipAddress
     * @return
     */
    private int calculator(String ipAddress) {
        logger.info("Convert IPADDRESS in octet");
        int result = 0;
        for(String part : ipAddress.split(Pattern.quote("."))) {

            result = result << 8;
            result |= Integer.parseInt(part);
        }
        logger.info("Converted IPADDRESS "+ipAddress +" in octet: "+ result);
        return result;
    }

    /**
     * This method will check if provided IP address is in defined IP range or not. Method will
     * return true/false based on input parameter.
     *
     * @param lowerBound
     * @param upperBound
     * @param rangeIPAddress
     * @return
     */
    public boolean isIPAddressInRange(String lowerBound, String upperBound, String rangeIPAddress) {
        logger.info("isIPAddressInRange() has been invoked");

        int lower_Bound = 0;
        int upper_Bound = 0;
        int range_ip_address = 0;

        lower_Bound = calculator(lowerBound);
        upper_Bound = calculator(upperBound);
        range_ip_address = calculator(rangeIPAddress);

        if(range_ip_address >= lower_Bound && range_ip_address <= upper_Bound)
            return true;
        return false;
    }

    /**
     * This method will generate no of IP based on lower bound and upper bound Ip address.
     *
     * @param lowerBound
     * @param upperBound
     * @param noOfIP
     * @return
     * @throws AddressStringException
     */
    public List<String> getRequestedNoOfIpFromGivenRange(String lowerBound, String upperBound, int noOfIP) throws AddressStringException {
        logger.info("getRequestedNoOfIpFromGivenRange() has been invoked");

        logger.info("lowerBound: "+lowerBound+" upperBound: "+upperBound+" noOfIP: "+noOfIP);
        try
        {
            List<String> requestedIPAddressList = new ArrayList<>();

            IPAddress lower = new IPAddressString(lowerBound).toAddress().incrementBoundary(1);

            IPAddress upper = new IPAddressString(upperBound).toAddress();
            IPAddressSeqRange range = lower.toSequentialRange(upper);

            for(IPAddress addr : range.getIterable()) {
                if(noOfIP != 0)
                {
                    requestedIPAddressList.add(addr.toString());
                    noOfIP--;
                } else {
                    break;
                }
            }
            logger.info("Request No. Of IP's From defined pool is : " + requestedIPAddressList);
            return requestedIPAddressList;
        }
        catch (Exception exception)
        {
            logger.error("Failed to generate IP's from given range.");
            throw new RuntimeException(exception.getMessage(), exception);
        }
    }
}
