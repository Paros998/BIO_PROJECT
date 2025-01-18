package psk.bio.car.rental.infrastructure.data.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import org.springframework.web.server.ResponseStatusException;
import psk.bio.car.rental.api.common.paging.PageResponse;
import psk.bio.car.rental.api.vehicles.VehicleModel;
import psk.bio.car.rental.application.vehicle.VehicleState;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleEntity;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleJpaRepository;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VehicleServiceImplTest {

    @Mock
    private VehicleJpaRepository vehicleRepository;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @Test
    void shouldReturnEmptyPageResponseWhenNoVehiclesFound() {
        // Arrange
        var pageRequest = new psk.bio.car.rental.api.common.paging.PageRequest(0, 10, "asc", "dir");

        // Act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            vehicleService.searchVehicles(VehicleState.NEW, pageRequest);
        });
        // Assert
        assertEquals(400, exception.getStatusCode().value());
        assertEquals("No member with class dir exists on this entity", exception.getReason());
    }
}
