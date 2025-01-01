package psk.bio.car.rental.api.employees;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@SuperBuilder
@EqualsAndHashCode
public class EmployeeModel implements Serializable {
    private UUID userId;
    private UUID employeeId;
    private String email;
    private Boolean isActive;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String nationalId;
}
