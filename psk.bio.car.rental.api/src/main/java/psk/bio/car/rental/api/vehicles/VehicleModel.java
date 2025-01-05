package psk.bio.car.rental.api.vehicles;

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
public class VehicleModel implements Serializable {
    private UUID vehicleId;
    private String model;
    private String plate;
    private String color;
    private Integer yearOfProduction;
    private VehicleState state;
    private String rentPerDayPrice;
}
