package go.ip.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IPAddressReserveRequest {
    private Optional<Integer> noOfIpAddress;
    @NonNull
    private int ipPoolId;
    private Optional<String> ipAddress;
}
