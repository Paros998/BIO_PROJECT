package psk.bio.car.rental.infrastructure.data.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import psk.bio.car.rental.api.common.paging.PageResponse;
import psk.bio.car.rental.api.vehicles.VehicleModel;
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
        var pageRequest = new psk.bio.car.rental.api.common.paging.PageRequest(0, 10, null, null);
        var springPageRequest = PageRequest.of(0, 10);

        // Mockujemy pusty wynik
        Page<VehicleEntity> emptyPage = new PageImpl<>(Collections.emptyList(), springPageRequest, 0);
        when(vehicleRepository.findAll(springPageRequest)).thenReturn(emptyPage);

        // Act
        PageResponse<VehicleModel> result = vehicleService.searchVehicles(null, pageRequest);

        // Assert
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, (int) result.getTotalPages()); // Używamy istniejących metod
        assertEquals(0, (int) result.getCurrentPage());
    }
}
