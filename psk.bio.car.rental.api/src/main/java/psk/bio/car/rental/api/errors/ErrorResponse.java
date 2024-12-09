package psk.bio.car.rental.api.errors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ErrorResponse implements Serializable {
    private int status;
    private String exception;
    private String businessError;
    private String uriPath;
    private String stackTrace;
    @JsonIgnore // TODO fix later
    private LocalDateTime timestamp;
}
