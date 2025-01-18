package psk.bio.car.rental.infrastructure.data.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import psk.bio.car.rental.application.vehicle.VehicleState;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceImplTest {

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

  @Test
  void shouldThrowExceptionForInvalidSortDirection() {
    // Arrange
    var pageRequest = new psk.bio.car.rental.api.common.paging.PageRequest(0, 10, "invalid", "id");

    // Act & Assert
    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      vehicleService.searchVehicles(VehicleState.NEW, pageRequest);
    });
    assertEquals(400, exception.getStatusCode().value());
    assertEquals("Sort Direction must be ASC or DESC", exception.getReason());
  }

  @Test
  void shouldThrowExceptionForInvalidPageNumber() {
    // Arrange
    var pageRequest = new psk.bio.car.rental.api.common.paging.PageRequest(0, 10, "asc", "id");

    // Act & Assert
    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      vehicleService.searchVehicles(VehicleState.NEW, pageRequest);
    });
    assertEquals(400, exception.getStatusCode().value());
    assertEquals("Page Number cannot be less than 1", exception.getReason());
  }

  @Test
  void shouldThrowExceptionForInvalidPageSize() {
    // Arrange
    var pageRequest = new psk.bio.car.rental.api.common.paging.PageRequest(1, 0, "asc", "id");

    // Act & Assert
    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      vehicleService.searchVehicles(VehicleState.NEW, pageRequest);
    });
    assertEquals(400, exception.getStatusCode().value());
    assertEquals("Page Size cannot be less than -1 or equal 0", exception.getReason());
  }
}