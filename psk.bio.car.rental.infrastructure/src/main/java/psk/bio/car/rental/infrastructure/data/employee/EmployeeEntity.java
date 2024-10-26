package psk.bio.car.rental.infrastructure.data.employee;

import jakarta.persistence.Column;
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
    @Column(unique = true, nullable = false)
    private String employeeIdentifier;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public EmployeeEntity() {
        this.role = UserRole.EMPLOYEE;
    }
}