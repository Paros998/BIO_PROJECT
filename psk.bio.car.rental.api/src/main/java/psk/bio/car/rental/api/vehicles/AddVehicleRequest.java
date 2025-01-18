package psk.bio.car.rental.api.vehicles;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
@EqualsAndHashCode
public class AddVehicleRequest implements Serializable {
    private String model;
    private String plate;
    private String color;
    private Integer yearOfProduction;
    private BigDecimal rentPerDayPrice;
}
