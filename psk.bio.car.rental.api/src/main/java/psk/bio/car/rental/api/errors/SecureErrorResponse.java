package psk.bio.car.rental.api.errors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SecureErrorResponse implements Serializable {
    private int status;
    private String code;
    private String businessError;
    @JsonIgnore // TODO fix later
    private LocalDateTime timestamp;
}
