package psk.bio.car.rental.infrastructure.data.client;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
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
@Entity(name = "clients")
@Table(name = "clients")
@ToString(callSuper = true)
@SuperBuilder
public class ClientEntity extends UserEntity {

    @JsonManagedReference
    @ToString.Exclude
    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY)
    private List<RentalEntity> rentedVehicles = new ArrayList<>();

    @JsonManagedReference
    @ToString.Exclude
    @OneToMany(mappedBy = "chargedClient", fetch = FetchType.LAZY)
    private List<PaymentEntity> clientPayments = new ArrayList<>();

    public ClientEntity() {
        this.role = UserRole.CLIENT;
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
        ClientEntity that = (ClientEntity) o;
        return Objects.equals(rentedVehicles, that.rentedVehicles) && Objects.equals(clientPayments, that.clientPayments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rentedVehicles, clientPayments);
    }
}
