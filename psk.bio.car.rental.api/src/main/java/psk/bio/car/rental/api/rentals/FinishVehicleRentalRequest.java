package psk.bio.car.rental.api.rentals;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
@EqualsAndHashCode
public class FinishVehicleRentalRequest implements Serializable {
    @NonNull
    private UUID rentalId;
    @NonNull
    private UUID employeeId;
}
