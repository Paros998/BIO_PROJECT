package psk.bio.car.rental.api.employees;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@EqualsAndHashCode
public class FinishFirstLoginRequest {
    @NonNull
    private UUID employeeId;

    @NonNull
    private String password;
}
