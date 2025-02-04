package psk.bio.car.rental.infrastructure.data.vehicle;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import psk.bio.car.rental.application.rental.Rental;
import psk.bio.car.rental.application.rental.RentalState;
import psk.bio.car.rental.application.security.exceptions.BusinessExceptionFactory;
import psk.bio.car.rental.application.vehicle.*;
import psk.bio.car.rental.infrastructure.data.payments.PaymentEntity;
import psk.bio.car.rental.infrastructure.data.rentals.RentalEntity;

import java.math.BigDecimal;
import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static psk.bio.car.rental.application.security.exceptions.BusinessExceptionCodes.*;

@Getter
@Setter
@Entity(name = "vehicles")
@Table(name = "vehicles")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class VehicleEntity implements NewVehicle, InRepairVehicle, ReadyToRentVehicle, RentedVehicle, ReturnedVehicle, NotInsuredVehicle {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    @Column(
            nullable = false,
            updatable = false
    )
    private UUID id;

    @Column(unique = true, nullable = false)
    private String plate;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private BigDecimal rentPerDayPrice;

    private String externalInsuranceId;

    @Column(nullable = false)
    private Year yearOfProduction;

    private LocalDate ensuredOnDate;

    private LocalDate ensuredDueDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VehicleState state;

    private LocalDateTime lastStartRepairDate;

    private LocalDateTime lastEndRepairDate;

    private LocalDateTime lastStartRentDate;

    private LocalDateTime lastEndRentDate;

    @JsonBackReference
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private RentalEntity lastRental;

    @JsonManagedReference
    @OneToMany(mappedBy = "associatedVehicle", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<PaymentEntity> vehicleAssociatedPayments = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<RentalEntity> vehicleRentals = new ArrayList<>();

    @Override
    public VehicleEntity finishRepairsAndMakeReadyToRent() {
        this.lastEndRepairDate = LocalDateTime.now();
        this.state = VehicleState.READY_TO_RENT;
        return this;
    }

    @Override
    public VehicleEntity insureVehicle(final @NonNull String insuranceId, final @NonNull LocalDate dueDate) {
        if (dueDate.isBefore(LocalDate.now()) || dueDate.isEqual(LocalDate.now())) {
            throw BusinessExceptionFactory.instantiateBusinessException(VEHICLE_INSURANCE_DUE_DATE_IS_INCORRECT);
        }
        this.externalInsuranceId = insuranceId;
        this.ensuredOnDate = LocalDate.now();
        this.ensuredDueDate = dueDate;
        this.state = VehicleState.INSURED;
        return this;
    }

    @Override
    public VehicleEntity rentVehicle(final @NonNull Rental rental) {
        if (getCurrentRental() != null) {
            throw BusinessExceptionFactory.instantiateBusinessException(VEHICLE_IS_ALREADY_RENTED);
        }

        this.lastStartRentDate = LocalDateTime.now();
        this.vehicleRentals.add((RentalEntity) rental);
        this.state = VehicleState.RENTED;
        return this;
    }

    @Override
    public VehicleEntity insuranceRevoked() {
        this.externalInsuranceId = null;
        this.ensuredOnDate = null;
        this.ensuredDueDate = null;
        this.state = VehicleState.NOT_INSURED;
        return this;
    }

    @Override
    public VehicleEntity returnVehicle(final @NonNull Rental rental) {
        final Rental currentRental = getCurrentRental();
        if (currentRental == null) {
            throw BusinessExceptionFactory.instantiateBusinessException(VEHICLE_IS_NOT_RENTED);
        }

        this.lastRental = (RentalEntity) rental;
        this.lastEndRentDate = LocalDateTime.now();
        this.state = VehicleState.JUST_RETURNED;
        return this;
    }

    @Nullable
    @Override
    public RentalEntity getCurrentRental() {
        return getVehicleRentals().stream()
                .filter(rental -> rental.getState().equals(RentalState.ACTIVE))
                .findFirst()
                .orElse(null);
    }

    @Override
    public VehicleEntity sendToRepairVehicle() {
        this.lastStartRepairDate = LocalDateTime.now();
        this.state = VehicleState.IN_REPAIR;
        return this;
    }

    @Override
    public VehicleEntity makeAvailableToRentVehicle() {
        this.lastEndRentDate = LocalDateTime.now();
        this.state = VehicleState.READY_TO_RENT;
        return this;
    }

    @Override
    public UUID getVehicleId() {
        return id;
    }

    @Override
    public BigDecimal getRentPrice() {
        return rentPerDayPrice;
    }

    @Override
    public boolean isRented() {
        return getCurrentRental() != null && state != VehicleState.RENTED;
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this)
                .getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        VehicleEntity that = (VehicleEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(plate, that.plate) && Objects.equals(model, that.model)
                && Objects.equals(color, that.color) && Objects.equals(externalInsuranceId, that.externalInsuranceId)
                && state == that.state && Objects.equals(lastStartRepairDate, that.lastStartRepairDate)
                && Objects.equals(lastEndRepairDate, that.lastEndRepairDate)
                && Objects.equals(vehicleAssociatedPayments, that.vehicleAssociatedPayments);
    }

}
