package psk.bio.car.rental.application.vehicle;

import psk.bio.car.rental.api.common.paging.PageRequest;
import psk.bio.car.rental.api.common.paging.PageResponse;
import psk.bio.car.rental.api.vehicles.VehicleModel;

public interface VehicleService {
    PageResponse<VehicleModel> searchVehicles(VehicleState vehicleState, PageRequest pageRequest);
}
