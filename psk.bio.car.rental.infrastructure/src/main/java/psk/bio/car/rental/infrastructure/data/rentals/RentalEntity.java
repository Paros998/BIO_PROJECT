package psk.bio.car.rental.infrastructure.data.rentals;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import psk.bio.car.rental.application.rental.RentalProjection;
import psk.bio.car.rental.application.rental.RentalState;
import psk.bio.car.rental.infrastructure.data.client.ClientEntity;
import psk.bio.car.rental.infrastructure.data.employee.EmployeeEntity;
import psk.bio.car.rental.infrastructure.data.payments.PaymentEntity;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "rentals")
@Table(name = "rentals")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalEntity implements RentalProjection {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(
            nullable = false,
            updatable = false
    )
    private UUID id;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private RentalState state;

    @JsonBackReference
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private ClientEntity client;

    @JsonBackReference
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private EmployeeEntity participatingEmployee;

    @JsonBackReference
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private VehicleEntity vehicle;

    @JsonBackReference
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY)
    private List<PaymentEntity> associatedPayments = new ArrayList<>();

    @Override
    public void finishRental() {
        this.endDate = LocalDateTime.now();
        this.state = RentalState.FINISHED;
    }

    @Override
    public String getRentalId() {
        return id.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RentalEntity that = (RentalEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(startDate, that.startDate)
                && Objects.equals(endDate, that.endDate) && Objects.equals(client, that.client)
                && Objects.equals(participatingEmployee, that.participatingEmployee) && Objects.equals(vehicle, that.vehicle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startDate, endDate, client, participatingEmployee, vehicle);
    }
}
