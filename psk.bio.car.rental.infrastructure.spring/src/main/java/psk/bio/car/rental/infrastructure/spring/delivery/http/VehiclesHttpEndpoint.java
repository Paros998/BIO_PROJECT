package psk.bio.car.rental.infrastructure.spring.delivery.http;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import psk.bio.car.rental.api.common.paging.PageRequest;
import psk.bio.car.rental.api.common.paging.PageResponse;
import psk.bio.car.rental.api.vehicles.VehicleModel;
import psk.bio.car.rental.api.vehicles.VehicleState;
import psk.bio.car.rental.application.vehicle.VehicleService;
import psk.bio.car.rental.infrastructure.data.common.paging.PageMapper;

import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/api/vehicles")
@RestController
public class VehiclesHttpEndpoint {
    private final VehicleService vehicleService;

    @GetMapping("/search")
    PageResponse<VehicleModel> searchVehicles(
            final @RequestParam(name = "state", required = false) VehicleState vehicleState,
            final @RequestParam(required = false, defaultValue = "1") Integer page,
            final @RequestParam(required = false, defaultValue = "10") Integer pageLimit,
            final @RequestParam(required = false, defaultValue = "desc") String sortDir,
            final @RequestParam(required = false, defaultValue = "yearOfProduction") String sortBy
    ) {
        final PageRequest pageRequest = PageMapper.toPageRequest(page, pageLimit, sortDir, sortBy);

        return vehicleService.searchVehicles(
                Optional.ofNullable(vehicleState)
                        .map(state -> psk.bio.car.rental.application.vehicle.VehicleState.valueOf(state.name()))
                        .orElse(null),
                pageRequest
        );
    }
}
