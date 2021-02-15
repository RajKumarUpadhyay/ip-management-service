package go.ip.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class GenerateIPAddressAndReserveRequest {
    @NonNull
    private int noOfIpAddress;
    @NonNull
    @ApiModelProperty(required = true)
    private int ipPoolId;
}
