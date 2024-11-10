package psk.bio.car.rental.infrastructure.data.vehicle;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import psk.bio.car.rental.application.vehicle.VehicleState;

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
public class VehicleEntity {

    @SuppressWarnings("checkstyle:VisibilityModifier")
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(
            nullable = false,
            updatable = false
    )
    protected UUID vehicleId;

    private String plate;

    private String model;

    private String color;

    private String externalInsuranceId;

    @Enumerated(EnumType.STRING)
    private VehicleState status;

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
        return Objects.equals(vehicleId, that.vehicleId) && Objects.equals(plate, that.plate) && Objects.equals(model, that.model)
                && Objects.equals(color, that.color) && Objects.equals(externalInsuranceId, that.externalInsuranceId)
                && status == that.status;
    }
}
