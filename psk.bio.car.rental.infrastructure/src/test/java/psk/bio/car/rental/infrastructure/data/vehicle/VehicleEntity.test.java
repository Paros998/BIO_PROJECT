package psk.bio.car.rental.infrastructure.data.vehicle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import psk.bio.car.rental.application.rental.Rental;
import psk.bio.car.rental.application.rental.RentalState;
import psk.bio.car.rental.application.security.exceptions.BusinessException;
import psk.bio.car.rental.application.vehicle.VehicleState;
import psk.bio.car.rental.infrastructure.data.rentals.RentalEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class VehicleEntityTest {

    private VehicleEntity vehicleEntity;

    @Mock
    private RentalEntity mockRentalEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        vehicleEntity = VehicleEntity.builder()
                .id(UUID.randomUUID())
                .plate("ABC123")
                .model("Test Model")
                .color("Red")
                .rentPerDayPrice(BigDecimal.valueOf(100))
                .yearOfProduction(Year.now())
                .state(VehicleState.READY_TO_RENT)
                .build();
    }

    @Test
    void testRentVehicle() {

    }
    
    @Test
void testFinishRepairsAndMakeReadyToRent() {
    // Arrange
    vehicleEntity.setState(VehicleState.IN_REPAIR);
    vehicleEntity.setLastEndRepairDate(null);

    // Act
    VehicleEntity result = vehicleEntity.finishRepairsAndMakeReadyToRent();

    // Assert
    assertEquals(VehicleState.READY_TO_RENT, result.getState());
    assertNotNull(result.getLastEndRepairDate());
    assertTrue(result.getLastEndRepairDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    assertTrue(result.getLastEndRepairDate().isAfter(LocalDateTime.now().minusSeconds(1)));
    assertSame(vehicleEntity, result);
}

@Test
void testFinishRepairsAndMakeReadyToRent_shouldReturnUpdatedInstance() {
    // Arrange
    vehicleEntity.setState(VehicleState.IN_REPAIR);
    vehicleEntity.setLastEndRepairDate(null);

    // Act
    VehicleEntity result = vehicleEntity.finishRepairsAndMakeReadyToRent();

    // Assert
    assertEquals(VehicleState.READY_TO_RENT, result.getState());
    assertNotNull(result.getLastEndRepairDate());
    assertTrue(result.getLastEndRepairDate().isAfter(LocalDateTime.now().minusSeconds(1)));
    assertTrue(result.getLastEndRepairDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    assertSame(vehicleEntity, result);
}

@Test
void testFinishRepairsAndMakeReadyToRent_shouldUpdateLastEndRepairDateEvenIfStateAlreadyReadyToRent() {
    // Arrange
    vehicleEntity.setState(VehicleState.READY_TO_RENT);
    LocalDateTime initialLastEndRepairDate = LocalDateTime.now().minusDays(1);
    vehicleEntity.setLastEndRepairDate(initialLastEndRepairDate);

    // Act
    VehicleEntity result = vehicleEntity.finishRepairsAndMakeReadyToRent();

    // Assert
    assertEquals(VehicleState.READY_TO_RENT, result.getState());
    assertNotNull(result.getLastEndRepairDate());
    assertTrue(result.getLastEndRepairDate().isAfter(initialLastEndRepairDate));
    assertTrue(result.getLastEndRepairDate().isBefore(LocalDateTime.now().plusSeconds(1)));
    assertTrue(result.getLastEndRepairDate().isAfter(LocalDateTime.now().minusSeconds(1)));
    assertSame(vehicleEntity, result);
}

@Test
void testRentVehicle_shouldThrowBusinessExceptionWhenVehicleIsAlreadyRented() {
    // Arrange
    RentalEntity existingRental = mock(RentalEntity.class);
    when(existingRental.getState()).thenReturn(RentalState.ACTIVE);
    vehicleEntity.getVehicleRentals().add(existingRental);

    Rental newRental = mock(Rental.class);

    // Act & Assert
    assertThrows(BusinessException.class, () -> vehicleEntity.rentVehicle(newRental));
    assertEquals(VehicleState.READY_TO_RENT, vehicleEntity.getState());
    assertNull(vehicleEntity.getLastStartRentDate());
    assertEquals(1, vehicleEntity.getVehicleRentals().size());
    verify(newRental, never()).getState();
}

@Test
void testRentVehicle_shouldThrowNullPointerExceptionWhenRentalIsNull() {
    // Arrange
    vehicleEntity.setState(VehicleState.READY_TO_RENT);
    vehicleEntity.setLastStartRentDate(null);

    // Act & Assert
    assertThrows(NullPointerException.class, () -> vehicleEntity.rentVehicle(null));
    assertEquals(VehicleState.READY_TO_RENT, vehicleEntity.getState());
    assertNull(vehicleEntity.getLastStartRentDate());
    assertTrue(vehicleEntity.getVehicleRentals().isEmpty());
}

@Test
void testRentVehicle_shouldNotAddRentalIfExceptionOccurs() {
    // Arrange
    RentalEntity existingRental = mock(RentalEntity.class);
    when(existingRental.getState()).thenReturn(RentalState.ACTIVE);
    vehicleEntity.getVehicleRentals().add(existingRental);

    Rental newRental = mock(Rental.class);
    int initialRentalsSize = vehicleEntity.getVehicleRentals().size();

    // Act & Assert
    assertThrows(BusinessException.class, () -> vehicleEntity.rentVehicle(newRental));
    assertEquals(initialRentalsSize, vehicleEntity.getVehicleRentals().size());
    verify(newRental, never()).getState();
}

@Test
void testGetCurrentRental_shouldReturnNullWhenNoRentals() {
    // Arrange
    vehicleEntity.setVehicleRentals(new ArrayList<>());

    // Act
    RentalEntity result = vehicleEntity.getCurrentRental();

    // Assert
    assertNull(result);
}

@Test
void testGetCurrentRental_shouldReturnNullWhenRentalListIsEmpty() {
    // Arrange
    vehicleEntity.setVehicleRentals(new ArrayList<>());

    // Act
    RentalEntity result = vehicleEntity.getCurrentRental();

    // Assert
    assertNull(result);
}

@Test
void testGetVehicleId_shouldReturnCorrectUUID() {
    // Arrange
    UUID expectedId = UUID.randomUUID();
    vehicleEntity.setId(expectedId);

    // Act
    UUID result = vehicleEntity.getVehicleId();

    // Assert
    assertEquals(expectedId, result);
}
@Test
void testGetVehicleId_shouldReturnNonNullUUID() {
    // Arrange
    UUID expectedId = UUID.randomUUID();
    vehicleEntity.setId(expectedId);

    // Act
    UUID result = vehicleEntity.getVehicleId();

    // Assert
    assertNotNull(result);
    assertEquals(expectedId, result);
}
@Test
void testIsRented_shouldReturnTrueWhenCurrentRentalIsNotNullAndStateIsNotRented() {
    // Arrange
    RentalEntity mockRental = mock(RentalEntity.class);
    when(mockRental.getState()).thenReturn(RentalState.ACTIVE);
    vehicleEntity.getVehicleRentals().add(mockRental);
    vehicleEntity.setState(VehicleState.READY_TO_RENT);

    // Act
    boolean result = vehicleEntity.isRented();

    // Assert
    assertTrue(result);
    verify(mockRental).getState();
}
@Test
void testIsRented_shouldReturnFalseWhenCurrentRentalIsNotNullButStateIsRented() {
    // Arrange
    RentalEntity mockRental = mock(RentalEntity.class);
    when(mockRental.getState()).thenReturn(RentalState.ACTIVE);
    vehicleEntity.getVehicleRentals().add(mockRental);
    vehicleEntity.setState(VehicleState.RENTED);

    // Act
    boolean result = vehicleEntity.isRented();

    // Assert
    assertFalse(result);
    verify(mockRental).getState();
}
}
