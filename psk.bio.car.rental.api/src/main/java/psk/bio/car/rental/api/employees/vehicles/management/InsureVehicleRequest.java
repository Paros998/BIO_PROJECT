package psk.bio.car.rental.api.employees.vehicles.management;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class InsureVehicleRequest extends EmployeeVehicleManagementRequest implements Serializable {
    @NonNull
    private String insuranceId;
    @NonNull
    private String bankAccountNumber;
    @NonNull
    private BigDecimal insuranceCost;
    @NonNull
    private LocalDate dueDate;
}
