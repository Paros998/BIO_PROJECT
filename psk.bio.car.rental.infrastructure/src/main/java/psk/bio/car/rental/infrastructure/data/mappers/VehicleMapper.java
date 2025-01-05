package psk.bio.car.rental.infrastructure.data.mappers;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import psk.bio.car.rental.api.rentals.RentalModel;
import psk.bio.car.rental.api.vehicles.RentedVehicle;
import psk.bio.car.rental.api.vehicles.VehicleModel;
import psk.bio.car.rental.application.rental.Rental;
import psk.bio.car.rental.application.vehicle.Vehicle;

import java.math.BigDecimal;
import java.util.Optional;

@UtilityClass
public class VehicleMapper {

    public VehicleModel toVehicleModel(final @NonNull Vehicle vehicle) {
        return VehicleModel.builder()
                .vehicleId(vehicle.getVehicleId())
                .model(vehicle.getModel())
                .plate(vehicle.getPlate())
                .color(vehicle.getColor())
                .state(psk.bio.car.rental.api.vehicles.VehicleState.valueOf(vehicle.getState().name()))
                .yearOfProduction(vehicle.getYearOfProduction().getValue())
                .rentPerDayPrice(
                        Optional.ofNullable(vehicle.getRentPrice())
                                .map(BigDecimal::toEngineeringString)
                                .orElse(null))
                .build();
    }

    public RentedVehicle toRentedVehicle(final @NonNull Vehicle vehicle, final @NonNull Rental rental) {
        return RentedVehicle.builder()
                .vehicle(toVehicleModel(vehicle))
                .rental(RentalModel.builder()
                        .rentalId(rental.getRentalId())
                        .vehicleId(rental.getVehicleId())
                        .clientId(rental.getClientId())
                        .approvingEmployeeId(rental.getApprovingEmployeeId())
                        .startDate(rental.getRentStartDate())
                        .endDate(rental.getRentEndDate())
                        .paymentsFeesPaid(rental.areAllPaymentsFeesPaid())
                        .build())
                .build();
    }
}
