package psk.bio.car.rental.infrastructure.data.vehicle;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import psk.bio.car.rental.application.payments.PaymentStatus;
import psk.bio.car.rental.application.payments.PaymentType;
import psk.bio.car.rental.application.rental.Rental;
import psk.bio.car.rental.application.rental.RentalState;
import psk.bio.car.rental.application.vehicle.*;
import psk.bio.car.rental.infrastructure.data.client.ClientEntity;
import psk.bio.car.rental.infrastructure.data.payments.PaymentEntity;
import psk.bio.car.rental.infrastructure.data.rentals.RentalEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "vehicles")
@Table(name = "vehicles")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class VehicleEntity implements NewVehicle, InRepairVehicle, ReadyToRentVehicle, RentedVehicle, ReturnedVehicle {
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
    public ReadyToRentVehicle finishRepairs(final @NonNull BigDecimal totalCost, final @NonNull LocalDate dueDate,
                                            final String chargedClientId) {
        this.lastEndRepairDate = LocalDateTime.now();
        var client = ClientEntity.builder()
                .userId(UUID.fromString(chargedClientId))
                .build();
        var payment = PaymentEntity.builder()
                .status(PaymentStatus.PENDING)
                .type(PaymentType.CLIENT_REPAIR)
                .amount(totalCost)
                .creationDate(LocalDateTime.now())
                .dueDate(dueDate)
                .chargedClient(client)
                .associatedVehicle(this)
                .build();
        this.vehicleAssociatedPayments.add(payment);
        this.state = VehicleState.READY_TO_RENT;
        return this;
    }

    @Override
    public ReadyToRentVehicle finishRepairs(final @NonNull BigDecimal totalCost, final @NonNull LocalDate dueDate) {
        this.lastEndRepairDate = LocalDateTime.now();
        var payment = PaymentEntity.builder()
                .status(PaymentStatus.PENDING)
                .type(PaymentType.REPAIR)
                .amount(totalCost)
                .creationDate(LocalDateTime.now())
                .dueDate(dueDate)
                .associatedVehicle(this)
                .build();
        this.vehicleAssociatedPayments.add(payment);
        this.state = VehicleState.READY_TO_RENT;
        return this;
    }

    @Override
    public ReadyToRentVehicle insureVehicle(final String insuranceId, final LocalDate dueDate) {
        this.externalInsuranceId = insuranceId;
        this.ensuredOnDate = LocalDate.now();
        this.ensuredDueDate = dueDate;
        this.state = VehicleState.READY_TO_RENT;
        return this;
    }

    @Override
    public RentedVehicle rentVehicle(final Rental rental) {
        if (getCurrentRental() != null) {
            throw new RuntimeException("Vehicle is already rented.");
        }

        this.lastStartRentDate = LocalDateTime.now();
        this.vehicleRentals.add((RentalEntity) rental);
        this.state = VehicleState.RENTED;
        return this;
    }

    @Override
    public ReturnedVehicle returnVehicle() {
        final Rental currentRental = getCurrentRental();
        if (currentRental == null) {
            throw new RuntimeException("Vehicle is not rented.");
        }

        this.lastEndRentDate = LocalDateTime.now();
        currentRental.finishRental();
        this.state = VehicleState.JUST_RETURNED;
        return this;
    }

    @Nullable
    @Override
    public Rental getCurrentRental() {
        return getVehicleRentals().stream()
                .filter(rental -> rental.getState().equals(RentalState.ACTIVE))
                .findFirst()
                .orElse(null);
    }

    @Override
    public InRepairVehicle sendToRepairVehicle() {
        this.lastStartRepairDate = LocalDateTime.now();
        this.state = VehicleState.IN_REPAIR;
        return this;
    }

    @Override
    public ReadyToRentVehicle makeAvailableToRentVehicle() {
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
