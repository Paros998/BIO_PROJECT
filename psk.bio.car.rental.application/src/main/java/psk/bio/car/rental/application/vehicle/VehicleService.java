package psk.bio.car.rental.application.vehicle;

import lombok.NonNull;
import psk.bio.car.rental.api.common.paging.PageRequest;
import psk.bio.car.rental.api.common.paging.PageResponse;
import psk.bio.car.rental.api.vehicles.AddVehicleRequest;
import psk.bio.car.rental.api.vehicles.VehicleModel;

import java.util.UUID;

public interface VehicleService {
    PageResponse<VehicleModel> searchVehicles(VehicleState vehicleState, PageRequest pageRequest);

    UUID registerNewVehicle(@NonNull AddVehicleRequest request);

    @NonNull
    ReadyToRentVehicle findReadyToRentVehicle(@NonNull UUID vehicleId);
}
