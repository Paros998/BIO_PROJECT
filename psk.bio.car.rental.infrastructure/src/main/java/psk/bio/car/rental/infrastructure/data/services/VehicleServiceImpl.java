package psk.bio.car.rental.infrastructure.data.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import psk.bio.car.rental.api.common.paging.PageRequest;
import psk.bio.car.rental.api.common.paging.PageResponse;
import psk.bio.car.rental.api.vehicles.AddVehicleRequest;
import psk.bio.car.rental.api.vehicles.VehicleModel;
import psk.bio.car.rental.application.security.exceptions.BusinessExceptionFactory;
import psk.bio.car.rental.application.vehicle.NewVehicle;
import psk.bio.car.rental.application.vehicle.ReadyToRentVehicle;
import psk.bio.car.rental.application.vehicle.VehicleService;
import psk.bio.car.rental.application.vehicle.VehicleState;
import psk.bio.car.rental.infrastructure.data.common.paging.PageMapper;
import psk.bio.car.rental.infrastructure.data.common.paging.SpringPageRequest;
import psk.bio.car.rental.infrastructure.data.common.paging.SpringPageResponse;
import psk.bio.car.rental.infrastructure.data.mappers.VehicleMapper;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleEntity;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleJpaRepository;

import java.time.Year;
import java.util.UUID;

import static psk.bio.car.rental.application.security.exceptions.BusinessExceptionCodes.VEHICLE_WITH_SAME_PLATE_ALREADY_EXISTS;

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

    @Override
    public UUID registerNewVehicle(final @NonNull AddVehicleRequest request) {
        if (vehicleRepository.findVehicleByPlate(request.getPlate()).isPresent()) {
            throw BusinessExceptionFactory.instantiateBusinessException(VEHICLE_WITH_SAME_PLATE_ALREADY_EXISTS);
        }

        final var vehicle = VehicleEntity.builder()
                .state(VehicleState.NEW)
                .plate(request.getPlate())
                .color(request.getColor())
                .model(request.getModel())
                .rentPerDayPrice(request.getRentPerDayPrice())
                .yearOfProduction(Year.of(request.getYearOfProduction()))
                .build();

        return vehicleRepository.save((NewVehicle) vehicle).getVehicleId();
    }

    @Override
    public @NonNull ReadyToRentVehicle findReadyToRentVehicle(final @NonNull UUID vehicleId) {
        return vehicleRepository.findByIdAndState(vehicleId, VehicleState.READY_TO_RENT)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
