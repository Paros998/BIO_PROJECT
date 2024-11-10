package psk.bio.car.rental.infrastructure.data.employee;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import psk.bio.car.rental.application.security.UserRole;
import psk.bio.car.rental.infrastructure.data.rentals.RentalEntity;
import psk.bio.car.rental.infrastructure.data.user.UserEntity;

import java.util.List;
import java.util.Objects;


@Getter
@Setter
@Entity(name = "employees")
@Table(name = "employees")
@ToString(callSuper = true)
@SuperBuilder
public class EmployeeEntity extends UserEntity {
    @Column(unique = true, nullable = false)
    private String employeeIdentifier;

    @JsonManagedReference
    @ToString.Exclude
    @OneToMany(mappedBy = "participatingEmployee", fetch = FetchType.LAZY)
    private List<RentalEntity> rentedVehicles;

    public EmployeeEntity() {
        this.role = UserRole.EMPLOYEE;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        EmployeeEntity that = (EmployeeEntity) o;
        return Objects.equals(employeeIdentifier, that.employeeIdentifier) && Objects.equals(rentedVehicles, that.rentedVehicles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), employeeIdentifier, rentedVehicles);
    }
}
