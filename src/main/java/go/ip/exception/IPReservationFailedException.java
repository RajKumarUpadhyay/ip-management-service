package go.ip.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IPReservationFailedException extends RuntimeException {
    public IPReservationFailedException(String errorMessage) {
        super(errorMessage);
    }

    public IPReservationFailedException(String errorMessage, Throwable cause) {
        super(errorMessage, cause);
    }
}
