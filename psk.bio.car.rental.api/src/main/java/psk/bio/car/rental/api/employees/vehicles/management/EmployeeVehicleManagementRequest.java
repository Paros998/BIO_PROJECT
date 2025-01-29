package psk.bio.car.rental.api.employees.vehicles.management;

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
public class EmployeeVehicleManagementRequest implements Serializable {
    @NonNull
    private UUID vehicleId;
    @NonNull
    private UUID employeeId;
}
