package psk.bio.car.rental.infrastructure.data.payments;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import psk.bio.car.rental.application.payments.PaymentStatus;
import psk.bio.car.rental.application.payments.PaymentType;
import psk.bio.car.rental.infrastructure.data.client.ClientEntity;
import psk.bio.car.rental.infrastructure.data.employee.EmployeeEntity;
import psk.bio.car.rental.infrastructure.data.rentals.RentalEntity;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "payments")
@Table(name = "payments")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PaymentEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(
            nullable = false,
            updatable = false
    )
    private UUID id;

    private BigDecimal amount;

    private LocalDateTime creationDate;
    private LocalDate dueDate;

    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private PaymentType type;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @JsonBackReference
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private ClientEntity chargedClient;

    @JsonBackReference
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private VehicleEntity associatedVehicle;

    @JsonBackReference
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private RentalEntity associatedRental;

    @JsonBackReference
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private EmployeeEntity createdByEmployee;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PaymentEntity that = (PaymentEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(amount, that.amount) && Objects.equals(creationDate, that.creationDate)
                && Objects.equals(dueDate, that.dueDate) && Objects.equals(accountNumber, that.accountNumber)
                && type == that.type && status == that.status && Objects.equals(chargedClient, that.chargedClient)
                && Objects.equals(associatedVehicle, that.associatedVehicle) && Objects.equals(associatedRental, that.associatedRental)
                && Objects.equals(createdByEmployee, that.createdByEmployee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, creationDate, dueDate, accountNumber, type, status, chargedClient, associatedVehicle,
                associatedRental, createdByEmployee);
    }
}
