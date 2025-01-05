package psk.bio.car.rental.api.clients;

import lombok.*;
import lombok.experimental.SuperBuilder;
import psk.bio.car.rental.api.vehicles.RentedVehicle;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
@EqualsAndHashCode
public class ClientRentedVehicles implements Serializable {
    private ClientModel client;
    private List<RentedVehicle> vehicles;
}
