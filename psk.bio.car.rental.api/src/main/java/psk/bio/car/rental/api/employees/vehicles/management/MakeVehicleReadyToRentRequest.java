package psk.bio.car.rental.api.employees.vehicles.management;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(force = true)
public class MakeVehicleReadyToRentRequest extends EmployeeVehicleManagementRequest implements Serializable {
}
