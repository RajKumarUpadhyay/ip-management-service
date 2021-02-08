package go.ip.controller;

import go.ip.common.GenerateAndReserveRequest;
import go.ip.entities.IPAddress;
import go.ip.services.IpManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/ip-mgmt-service")
public class IpAddressMgmtController {

    @Autowired
    private IpManagementService ipManagementService;

    @RequestMapping(path = "/reserve-ip-address")
    public ResponseEntity<IPAddress> generateAndReserveIPAddress(@RequestBody GenerateAndReserveRequest generateAndReserveRequest) {
               ipManagementService.reserveIpAddress(generateAndReserveRequest);
            return new ResponseEntity<>(HttpStatus.OK);
    }
}
