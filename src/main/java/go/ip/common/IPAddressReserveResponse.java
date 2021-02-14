package go.ip.common;

import go.ip.entities.IPAddress;
import go.ip.entities.IPPool;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IPAddressReserveResponse {
    private IPAddress ipAddress;
    private IPPool ipPool;
}
