package psk.bio.car.rental.infrastructure.data.mappers;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import psk.bio.car.rental.api.rentals.RentalModel;
import psk.bio.car.rental.api.vehicles.RentedVehicle;
import psk.bio.car.rental.api.vehicles.VehicleModel;
import psk.bio.car.rental.application.rental.Rental;
import psk.bio.car.rental.application.security.UserRole;
import psk.bio.car.rental.application.vehicle.Vehicle;
import psk.bio.car.rental.application.vehicle.VehicleAction;
import psk.bio.car.rental.application.vehicle.VehicleState;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static psk.bio.car.rental.application.vehicle.VehicleAction.*;
import static psk.bio.car.rental.application.vehicle.VehicleState.*;

@UtilityClass
public class VehicleMapper {
    private final Map<VehicleState, Set<VehicleAction>> stateToActionsMap = Map.of(
            NEW, Set.of(INSURE),
            NOT_INSURED, Set.of(INSURE),
            INSURED, Set.of(SEND_TO_REPAIRS, MAKE_READY_TO_RENT),
            IN_REPAIR, Set.of(MAKE_READY_TO_RENT),
            RENTED, Set.of(RETURN_AND_SEND_TO_REPAIRS, RETURN_AND_MAKE_READY_TO_RENT)
    );

    public VehicleModel toVehicleModel(final @NonNull Vehicle vehicle, @NonNull final UserRole userRole) {
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
                .employeeActions(userRole == UserRole.CLIENT ? Set.of() : stateToActionsMap.get(vehicle.getState()).stream()
                        .map(action -> psk.bio.car.rental.api.vehicles.VehicleAction.valueOf(action.name()))
                        .collect(Collectors.toSet()))
                .build();
    }

    public RentedVehicle toRentedVehicle(final @NonNull Vehicle vehicle, final @NonNull Rental rental, @NonNull final UserRole userRole) {
        return RentedVehicle.builder()
                .vehicle(toVehicleModel(vehicle, userRole))
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
