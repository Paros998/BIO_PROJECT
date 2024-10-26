package psk.bio.car.rental.infrastructure.data.client;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import psk.bio.car.rental.application.security.UserRole;
import psk.bio.car.rental.infrastructure.data.user.UserEntity;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleEntity;

@Getter
@Setter
@Entity(name = "clients")
@Table(name = "clients")
@ToString(callSuper = true)
@SuperBuilder
public class ClientEntity extends UserEntity {
    /* TODO client domain data */

    public ClientEntity() {
        this.role = UserRole.CLIENT;
    }

    private String name;
    private String email;
    private String phone;
    private List<VehicleEntity> vehicles;
}
