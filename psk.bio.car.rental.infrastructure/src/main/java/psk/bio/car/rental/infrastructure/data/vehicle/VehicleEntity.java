package psk.bio.car.rental.infrastructure.data.vehicle;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import psk.bio.car.rental.application.rental.RentalProjection;
import psk.bio.car.rental.application.vehicle.*;
import psk.bio.car.rental.infrastructure.data.payments.PaymentEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "vehicles")
@Table(name = "vehicles")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class VehicleEntity implements NewVehicleProjection, InRepairVehicleProjection, ReadyToRentVehicleProjection,
        RentedVehicleProjection {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(
            nullable = false,
            updatable = false
    )
    private UUID id;

    private String plate;

    private String model;

    private String color;

    private String externalInsuranceId;

    @Enumerated(EnumType.STRING)
    private VehicleState status;

    private LocalDateTime startRepairDate;

    private LocalDateTime endRepairDate;

    @OneToMany()
    private List<PaymentEntity> vehicleAssociatedPayments = new ArrayList<>();

    @Override
    public ReadyToRentVehicleProjection finishRepairs(final BigDecimal totalCost) {
        this.endRepairDate = LocalDateTime.now();
        return null;
    }

    @Override
    public ReadyToRentVehicleProjection insureVehicle(String externalInsuranceId, LocalDateTime dueDate) {
        return null;
    }

    @Override
    public RentedVehicleProjection rentVehicle(String rentalId) {
        return null;
    }

    @Override
    public ReadyToRentVehicleProjection returnVehicle(String clientId) {
        return null;
    }

    @Override
    public RentalProjection getRental() {
        return null;
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this)
                .getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VehicleEntity that = (VehicleEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(plate, that.plate) && Objects.equals(model, that.model)
                && Objects.equals(color, that.color) && Objects.equals(externalInsuranceId, that.externalInsuranceId)
                && status == that.status;
    }
}
