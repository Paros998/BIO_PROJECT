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
public class FinishRepairsRequest extends EmployeeVehicleManagementRequest implements Serializable {
    @NonNull
    private Boolean chargeCustomer;
    @NonNull
    private BigDecimal totalCost;
    @NonNull
    private String bankAccountNumber;
    @NonNull
    private LocalDate dueDate;
}
