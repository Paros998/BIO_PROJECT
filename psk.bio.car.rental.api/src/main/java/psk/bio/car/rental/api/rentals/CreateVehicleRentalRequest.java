package psk.bio.car.rental.api.rentals;

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
public class CreateVehicleRentalRequest implements Serializable {
    @NonNull
    private UUID vehicleId;
    @NonNull
    private UUID clientId;
    @NonNull
    private Integer numberOfDays;
}
