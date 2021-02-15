package go.ip.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IPAddressRequest {
    private Optional<Integer> noOfIpAddress;
    @NonNull
    @ApiModelProperty(required = true)
    private int ipPoolId;
    private Optional<String> ipAddress;
}
