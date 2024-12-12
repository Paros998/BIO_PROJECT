package psk.bio.car.rental.api.vehicles;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
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
