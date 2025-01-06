package psk.bio.car.rental.api.employees;

import lombok.*;
import lombok.experimental.SuperBuilder;
import psk.bio.car.rental.api.security.RegisterRequest;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class CreateEmployeeRequest extends RegisterRequest {
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String phoneNumber;
    @NonNull
    private String nationalId;

}
