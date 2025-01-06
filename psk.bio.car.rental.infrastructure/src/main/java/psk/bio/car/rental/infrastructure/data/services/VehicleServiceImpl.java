package psk.bio.car.rental.infrastructure.data.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import psk.bio.car.rental.api.common.paging.PageRequest;
import psk.bio.car.rental.api.common.paging.PageResponse;
import psk.bio.car.rental.api.vehicles.VehicleModel;
import psk.bio.car.rental.application.vehicle.VehicleService;
import psk.bio.car.rental.application.vehicle.VehicleState;
import psk.bio.car.rental.infrastructure.data.common.paging.PageMapper;
import psk.bio.car.rental.infrastructure.data.common.paging.SpringPageRequest;
import psk.bio.car.rental.infrastructure.data.common.paging.SpringPageResponse;
import psk.bio.car.rental.infrastructure.data.mappers.VehicleMapper;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleEntity;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleJpaRepository;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
    private final VehicleJpaRepository vehicleRepository;

    public PageResponse<VehicleModel> searchVehicles(final VehicleState vehicleState, final PageRequest pageRequest) {
        SpringPageRequest springPageRequest = PageMapper.toSpringPageRequest(pageRequest);
        SpringPageResponse<VehicleEntity> vehicles;

        if (vehicleState != null) {
            vehicles = new SpringPageResponse<>(
                    vehicleRepository.findByState(vehicleState, springPageRequest.getRequest(VehicleEntity.class))
            );
        } else {
            vehicles = new SpringPageResponse<>(
                    vehicleRepository.findAll(springPageRequest.getRequest(VehicleEntity.class))
            );
        }

        return PageMapper.toPageResponse(vehicles, VehicleMapper::toVehicleModel);
    }
}
