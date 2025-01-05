package psk.bio.car.rental.api.rentals;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Builder
@EqualsAndHashCode
public class RentalModel implements Serializable {
    private UUID rentalId;
    private UUID clientId;
    private UUID vehicleId;
    private UUID approvingEmployeeId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean paymentsFeesPaid;
}
