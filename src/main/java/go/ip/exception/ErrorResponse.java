package go.ip.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String errorMessage;
    private LocalDate currentDate = LocalDate.now();
}
