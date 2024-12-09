package psk.bio.car.rental.infrastructure.data.admin;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import psk.bio.car.rental.application.security.UserRole;
import psk.bio.car.rental.infrastructure.data.employee.EmployeeEntity;

@Getter
@Setter
@Entity(name = "administrators")
@Table(name = "administrators")
@ToString(callSuper = true)
@SuperBuilder
public class AdminEntity extends EmployeeEntity {
    public AdminEntity() {
        this.role = UserRole.ADMIN;
    }
}
