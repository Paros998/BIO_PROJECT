package psk.bio.car.rental.infrastructure.data.employee;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import psk.bio.car.rental.application.security.UserRole;
import psk.bio.car.rental.infrastructure.data.payments.PaymentEntity;
import psk.bio.car.rental.infrastructure.data.rentals.RentalEntity;
import psk.bio.car.rental.infrastructure.data.user.UserEntity;

import java.util.ArrayList;
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
    @OneToMany(mappedBy = "participatingEmployee", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<RentalEntity> rentedVehicles = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "createdByEmployee", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<PaymentEntity> createdPayments = new ArrayList<>();

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
        return Objects.equals(employeeIdentifier, that.employeeIdentifier)
                && Objects.equals(rentedVehicles, that.rentedVehicles)
                && Objects.equals(createdPayments, that.createdPayments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), employeeIdentifier, rentedVehicles, createdPayments);
    }
}
