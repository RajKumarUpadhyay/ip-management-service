package go.ip.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateAndReserveRequest {
    private int noOfIpAddress;
    private int ipPoolId;
}
