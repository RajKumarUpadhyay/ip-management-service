package go.ip.exception;

import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class StandardException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException runtimeException, WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage(runtimeException.getMessage());
        errorResponse.setCause(runtimeException.getCause());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException, WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage(resourceNotFoundException.getMessage());
        errorResponse.setCause(resourceNotFoundException.getCause());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(SQLException sqlException, WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage(sqlException.getMessage());
        errorResponse.setCause(sqlException.getCause());
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(IPReservationFailedException.class)
    public ResponseEntity<Object> handleIPReservationFailedException(IPReservationFailedException ipReservationFailedException, WebRequest webRequest) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorMessage(ipReservationFailedException.getMessage());
        errorResponse.setCause(ipReservationFailedException.getCause());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
