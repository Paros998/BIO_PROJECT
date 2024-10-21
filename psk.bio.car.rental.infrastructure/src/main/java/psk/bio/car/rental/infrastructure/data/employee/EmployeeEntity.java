package psk.bio.car.rental.infrastructure.data.employee;

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
@Entity(name = "employees")
@Table(name = "employees")
@ToString(callSuper = true)
@SuperBuilder
public class EmployeeEntity extends UserEntity {
    private String employeeIdentifier;

    /* TODO employee domain data */

    public EmployeeEntity() {
        this.role = UserRole.EMPLOYEE;
    }
}