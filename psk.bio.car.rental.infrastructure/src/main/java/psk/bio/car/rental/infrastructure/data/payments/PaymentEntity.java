package psk.bio.car.rental.infrastructure.data.payments;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import psk.bio.car.rental.application.payments.PaymentStatus;
import psk.bio.car.rental.application.payments.PaymentType;
import psk.bio.car.rental.infrastructure.data.client.ClientEntity;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
}
