package go.ip.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IPCapacityOverflowException extends RuntimeException {

    public IPCapacityOverflowException(String errorMessage) {
        super(errorMessage);
    }

    public IPCapacityOverflowException(String errorMessage, Throwable throwable) {
        super(errorMessage, throwable);
    }
}
