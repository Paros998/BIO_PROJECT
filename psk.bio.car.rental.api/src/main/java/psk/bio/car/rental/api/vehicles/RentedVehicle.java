package psk.bio.car.rental.api.vehicles;

import lombok.*;
import lombok.experimental.SuperBuilder;
import psk.bio.car.rental.api.rentals.RentalModel;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
@EqualsAndHashCode
public class RentedVehicle implements Serializable {
    private VehicleModel vehicle;
    private RentalModel rental;
}
