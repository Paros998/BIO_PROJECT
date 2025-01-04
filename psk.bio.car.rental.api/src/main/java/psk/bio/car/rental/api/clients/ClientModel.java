package psk.bio.car.rental.api.clients;

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
public class ClientModel implements Serializable {
    private UUID userId;
    private String email;
    private Boolean isActive;
    private Boolean firstLoginDone;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String nationalId;
    private Integer rentedCarsCount;
    private Integer duePaymentsCount;
}
