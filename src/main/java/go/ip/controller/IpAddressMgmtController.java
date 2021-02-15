package go.ip.controller;

import go.ip.common.GenerateIPAddressAndReserveRequest;
import go.ip.common.IPAddressRequest;
import go.ip.common.IPAddressResponse;
import go.ip.common.ReserveOrBlacklistOrFreeIPAddressRequest;
import go.ip.services.IpManagementService;
import inet.ipaddr.AddressStringException;
import io.swagger.annotations.Api;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/ip-mgmt-service")
@Api
public class IpAddressMgmtController {

    public static final Logger logger = Logger.getLogger(IpAddressMgmtController.class);

    @Autowired
    private IpManagementService ipManagementService;

    @PostMapping(path = "/generateAndReserveIPAddress")
    public ResponseEntity<List<String>> generateAndReserveIPAddress(@RequestBody GenerateIPAddressAndReserveRequest generateIPAddressAndReserveRequest) throws AddressStringException {

        logger.info("Build request for generate IP address from specified pool..");

        IPAddressRequest ipAddressRequest = new IPAddressRequest();
        ipAddressRequest.setIpPoolId(generateIPAddressAndReserveRequest.getIpPoolId());
        ipAddressRequest.setNoOfIpAddress(java.util.Optional.of(generateIPAddressAndReserveRequest.getNoOfIpAddress()));

        logger.info("Generate And Reserve IPAddress method has been invoked with the request parameter: " + ipAddressRequest.toString());
        return new ResponseEntity(ipManagementService.generateAndReserveIPAddress(ipAddressRequest), HttpStatus.OK);
    }

    @PutMapping(path = "/reserveIPAddress")
    public ResponseEntity<String> reserveIPAddress(@RequestBody ReserveOrBlacklistOrFreeIPAddressRequest reserveOrBlacklistOrFreeIPAddressRequest) throws AddressStringException {

        logger.info("Build request for reserve IP address from specified pool..");

        IPAddressRequest ipAddressRequest = new IPAddressRequest();
        ipAddressRequest.setIpPoolId(reserveOrBlacklistOrFreeIPAddressRequest.getIpPoolId());
        ipAddressRequest.setIpAddress(java.util.Optional.of(reserveOrBlacklistOrFreeIPAddressRequest.getIpAddress()));

        logger.info("Reserve IPAddress method has been invoked with the request parameter: " + ipAddressRequest.toString());
        return new ResponseEntity(ipManagementService.reserveIPAddress(ipAddressRequest), HttpStatus.OK);
    }

    @PutMapping(path = "/blacklistIPAddress")
    public ResponseEntity<String> blacklistIPAddress(@RequestBody ReserveOrBlacklistOrFreeIPAddressRequest reserveOrBlacklistOrFreeIPAddressRequest) throws AddressStringException {

        logger.info("Build request for blacklist IP address from specified pool..");

        IPAddressRequest ipAddressRequest = new IPAddressRequest();
        ipAddressRequest.setIpPoolId(reserveOrBlacklistOrFreeIPAddressRequest.getIpPoolId());
        ipAddressRequest.setIpAddress(java.util.Optional.of(reserveOrBlacklistOrFreeIPAddressRequest.getIpAddress()));

        logger.info("Blacklist IPAddress method has been invoked with the request parameter: " + ipAddressRequest.toString());
        return new ResponseEntity(ipManagementService.blacklistIPAddress(ipAddressRequest), HttpStatus.OK);
    }

    @PutMapping(path = "/freeIPAddress")
    public ResponseEntity<String> freeIPAddress(@RequestBody ReserveOrBlacklistOrFreeIPAddressRequest reserveOrBlacklistOrFreeIPAddressRequest) throws AddressStringException {

        logger.info("Build request for free IP address from specified pool..");

        IPAddressRequest ipAddressRequest = new IPAddressRequest();
        ipAddressRequest.setIpPoolId(reserveOrBlacklistOrFreeIPAddressRequest.getIpPoolId());
        ipAddressRequest.setIpAddress(java.util.Optional.of(reserveOrBlacklistOrFreeIPAddressRequest.getIpAddress()));

        logger.info("Free IPAddress method has been invoked with the request parameter: " + ipAddressRequest.toString());
        return new ResponseEntity(ipManagementService.freeIPAddress(ipAddressRequest), HttpStatus.OK);
    }

    @PostMapping(path = "/getIPInfo")
    public ResponseEntity<IPAddressResponse> getIPInfo(@RequestBody ReserveOrBlacklistOrFreeIPAddressRequest reserveOrBlacklistOrFreeIPAddressRequest) throws AddressStringException {
        logger.info("Build request for get info of the IP address from specified pool..");

        IPAddressRequest ipAddressRequest = new IPAddressRequest();
        ipAddressRequest.setIpPoolId(reserveOrBlacklistOrFreeIPAddressRequest.getIpPoolId());
        ipAddressRequest.setIpAddress(java.util.Optional.of(reserveOrBlacklistOrFreeIPAddressRequest.getIpAddress()));

        logger.info("Free IPAddress method has been invoked with the request parameter: " + ipAddressRequest.toString());
        return new ResponseEntity(ipManagementService.getIpInfo(ipAddressRequest), HttpStatus.OK);
    }
}
