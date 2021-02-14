package go.ip.controller;

import go.ip.common.IPAddressReserveRequest;
import go.ip.common.IPAddressReserveResponse;
import go.ip.services.IpManagementService;
import inet.ipaddr.AddressStringException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/ip-mgmt-service")
public class IpAddressMgmtController {

    public static final Logger logger = Logger.getLogger(IpAddressMgmtController.class);

    @Autowired
    private IpManagementService ipManagementService;

    @RequestMapping(path = "/generateAndReserveIPAddress")
    @GetMapping
    public ResponseEntity<List<String>> generateAndReserveIPAddress(@RequestBody IPAddressReserveRequest ipAddressReserveRequest) throws AddressStringException {

        logger.info("Generate And Reserve IPAddress method has been invoked with the request parameter: " + ipAddressReserveRequest.toString());
        return new ResponseEntity(ipManagementService.generateAndReserveIPAddress(ipAddressReserveRequest), HttpStatus.OK);
    }

    @RequestMapping(path = "/reserveIPAddress")
    @PutMapping
    public ResponseEntity<String> reserveIPAddress(@RequestBody IPAddressReserveRequest ipAddressReserveRequest) throws AddressStringException {
        logger.info("Reserve IPAddress method has been invoked with the request parameter: " + ipAddressReserveRequest.toString());
        return new ResponseEntity(ipManagementService.reserveIPAddress(ipAddressReserveRequest), HttpStatus.OK);
    }

    @RequestMapping(path = "/blacklistIPAddress")
    @PutMapping
    public ResponseEntity<String> blacklistIPAddress(@RequestBody IPAddressReserveRequest ipAddressReserveRequest) throws AddressStringException {
        logger.info("Blacklist IPAddress method has been invoked with the request parameter: " + ipAddressReserveRequest.toString());
        return new ResponseEntity(ipManagementService.blacklistIPAddress(ipAddressReserveRequest), HttpStatus.OK);
    }

    @RequestMapping(path = "/freeIPAddress")
    @PutMapping
    public ResponseEntity<String> freeIPAddress(@RequestBody IPAddressReserveRequest ipAddressReserveRequest) throws AddressStringException {
        logger.info("Free IPAddress method has been invoked with the request parameter: " + ipAddressReserveRequest.toString());
        return new ResponseEntity(ipManagementService.freeIPAddress(ipAddressReserveRequest), HttpStatus.OK);
    }

    @RequestMapping(path = "/getIPInfo")
    @GetMapping
    public ResponseEntity<IPAddressReserveResponse> getIPInfo(@RequestBody IPAddressReserveRequest ipAddressReserveRequest) throws AddressStringException {
        logger.info("Free IPAddress method has been invoked with the request parameter: " + ipAddressReserveRequest.toString());
        return new ResponseEntity(ipManagementService.getIpInfo(ipAddressReserveRequest), HttpStatus.OK);
    }
}
