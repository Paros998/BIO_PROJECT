package psk.bio.car.rental.infrastructure.data.admin;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import psk.bio.car.rental.application.security.UserRole;
import psk.bio.car.rental.infrastructure.data.user.UserEntity;

@Getter
@Setter
@Entity
@SuperBuilder
public class AdminEntity extends UserEntity {
    public AdminEntity() {
        this.role = UserRole.ADMIN;
    }
}