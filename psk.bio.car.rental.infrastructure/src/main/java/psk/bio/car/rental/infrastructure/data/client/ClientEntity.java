package psk.bio.car.rental.infrastructure.data.client;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import psk.bio.car.rental.application.security.UserRole;
import psk.bio.car.rental.infrastructure.data.user.UserEntity;

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
}
