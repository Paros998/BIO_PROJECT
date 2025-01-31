package psk.bio.car.rental.infrastructure.data.rentals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import psk.bio.car.rental.application.rental.RentalState;
import psk.bio.car.rental.infrastructure.data.client.ClientEntity;
import psk.bio.car.rental.infrastructure.data.employee.EmployeeEntity;
import psk.bio.car.rental.infrastructure.data.rentals.RentalEntity;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleEntity;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class RentalEntityTest {

    private RentalEntity rentalEntity;

    private ClientEntity mockClient;

    private EmployeeEntity mockEmployee;

    private VehicleEntity mockVehicle;

    
    @Test
void shouldSetEndDateToCurrentDateTimeWhenFinishingRental() {
    // Arrange
    RentalEntity rental = new RentalEntity();
    EmployeeEntity employee = new EmployeeEntity();
    LocalDateTime beforeFinish = LocalDateTime.now();

    // Act
    rental.finishRental(employee);

    // Assert
    assertNotNull(rental.getEndDate());
    assertTrue(rental.getEndDate().isAfter(beforeFinish) || rental.getEndDate().isEqual(beforeFinish));
    assertTrue(rental.getEndDate().isBefore(LocalDateTime.now()) || rental.getEndDate().isEqual(LocalDateTime.now()));
    assertEquals(RentalState.FINISHED, rental.getState());
    assertEquals(employee, rental.getParticipatingEmployee());
}

@Test
void shouldThrowExceptionWhenFinishingRentalWithNullEmployee() {
    // Arrange
    RentalEntity rental = new RentalEntity();

    // Act & Assert
    assertThrows(NullPointerException.class, () -> rental.finishRental(null));
}

@Test
void shouldChangeRentalStateToFinishedWhenFinishingRental() {
    // Arrange
    RentalEntity rental = new RentalEntity();
    rental.setState(RentalState.ACTIVE);
    EmployeeEntity employee = new EmployeeEntity();

    // Act
    rental.finishRental(employee);

    // Assert
    assertEquals(RentalState.FINISHED, rental.getState());
}

@Test
void shouldNotModifyOtherRentalPropertiesWhenFinishingRental() {
    // Arrange
    RentalEntity rental = new RentalEntity();
    rental.setId(UUID.randomUUID());
    rental.setStartDate(LocalDateTime.now().minusDays(1));
    rental.setState(RentalState.ACTIVE);
    rental.setClient(new ClientEntity());
    rental.setVehicle(new VehicleEntity());
    
    EmployeeEntity employee = new EmployeeEntity();
    
    UUID originalId = rental.getId();
    LocalDateTime originalStartDate = rental.getStartDate();
    ClientEntity originalClient = rental.getClient();
    VehicleEntity originalVehicle = rental.getVehicle();

    // Act
    rental.finishRental(employee);

    // Assert
    assertEquals(originalId, rental.getId());
    assertEquals(originalStartDate, rental.getStartDate());
    assertEquals(originalClient, rental.getClient());
    assertEquals(originalVehicle, rental.getVehicle());
    assertNotNull(rental.getEndDate());
    assertEquals(RentalState.FINISHED, rental.getState());
    assertEquals(employee, rental.getParticipatingEmployee());
}

@Test
void shouldReturnCorrectUuidWhenGetRentalIdIsCalled() {
    // Arrange
    UUID expectedId = UUID.randomUUID();
    RentalEntity rental = new RentalEntity();
    rental.setId(expectedId);

    // Act
    UUID actualId = rental.getRentalId();

    // Assert
    assertEquals(expectedId, actualId);
}
@Test
void shouldReturnNullWhenIdIsNotSetAndGetRentalIdIsCalled() {
    // Arrange
    RentalEntity rental = new RentalEntity();

    // Act
    UUID result = rental.getRentalId();

    // Assert
    assertNull(result);
}
@Test
void shouldReturnCorrectUuidAfterExplicitlySet() {
    // Arrange
    UUID expectedId = UUID.randomUUID();
    RentalEntity rental = new RentalEntity();
    rental.setId(expectedId);

    // Act
    UUID actualId = rental.getRentalId();

    // Assert
    assertEquals(expectedId, actualId);
}
@Test
void shouldReturnNullWhenClientUserIdIsNull() {
    // Arrange
    RentalEntity rental = new RentalEntity();
    ClientEntity client = new ClientEntity();
    client.setUserId(null);
    rental.setClient(client);

    // Act
    UUID result = rental.getClientId();

    // Assert
    assertNull(result);
}
@Test
void shouldReturnCorrectUuidWhenGetVehicleIdIsCalled() {
    // Arrange
    UUID expectedId = UUID.randomUUID();
    VehicleEntity vehicle = new VehicleEntity();
    vehicle.setId(expectedId);
    RentalEntity rental = new RentalEntity();
    rental.setVehicle(vehicle);

    // Act
    UUID actualId = rental.getVehicleId();

    // Assert
    assertEquals(expectedId, actualId);
}
@Test
void shouldReturnSameUuidOnMultipleCallsToGetVehicleId() {
    // Arrange
    UUID expectedId = UUID.randomUUID();
    VehicleEntity vehicle = new VehicleEntity();
    vehicle.setId(expectedId);
    RentalEntity rental = new RentalEntity();
    rental.setVehicle(vehicle);

    // Act
    UUID firstCall = rental.getVehicleId();
    UUID secondCall = rental.getVehicleId();

    // Assert
    assertEquals(expectedId, firstCall);
    assertEquals(expectedId, secondCall);
    assertSame(firstCall, secondCall);
}
@Test
void shouldNotModifyVehicleOrItsIdWhenGetVehicleIdIsCalled() {
    // Arrange
    UUID expectedId = UUID.randomUUID();
    VehicleEntity vehicle = new VehicleEntity();
    vehicle.setId(expectedId);
    RentalEntity rental = new RentalEntity();
    rental.setVehicle(vehicle);

    // Act
    UUID firstCall = rental.getVehicleId();
    UUID secondCall = rental.getVehicleId();

    // Assert
    assertEquals(expectedId, firstCall);
    assertEquals(expectedId, secondCall);
    assertSame(vehicle, rental.getVehicle());
    assertEquals(expectedId, rental.getVehicle().getId());
}
@Test
void shouldReturnSameValueAsGetEndDateWhenGetRentEndDateIsCalled() {
    // Arrange
    RentalEntity rental = new RentalEntity();
    LocalDateTime expectedEndDate = LocalDateTime.now();
    rental.setEndDate(expectedEndDate);

    // Act
    LocalDateTime actualEndDate = rental.getRentEndDate();

    // Assert
    assertEquals(expectedEndDate, actualEndDate);
    assertEquals(rental.getEndDate(), actualEndDate);
}
@Test
void shouldReturnCorrectEndDateAfterExplicitlySet() {
    // Arrange
    RentalEntity rental = new RentalEntity();
    LocalDateTime expectedEndDate = LocalDateTime.now().plusDays(7);
    rental.setEndDate(expectedEndDate);

    // Act
    LocalDateTime actualEndDate = rental.getRentEndDate();

    // Assert
    assertEquals(expectedEndDate, actualEndDate);
    assertEquals(rental.getEndDate(), actualEndDate);
}
@Test
void shouldReturnCorrectEndDateAfterRentalIsFinished() {
    // Arrange
    RentalEntity rental = new RentalEntity();
    EmployeeEntity employee = new EmployeeEntity();
    LocalDateTime beforeFinish = LocalDateTime.now();

    // Act
    rental.finishRental(employee);
    LocalDateTime endDate = rental.getRentEndDate();

    // Assert
    assertNotNull(endDate);
    assertTrue(endDate.isAfter(beforeFinish) || endDate.isEqual(beforeFinish));
    assertTrue(endDate.isBefore(LocalDateTime.now()) || endDate.isEqual(LocalDateTime.now()));
    assertEquals(rental.getEndDate(), endDate);
}
@Test
void shouldReturnTrueWhenComparingRentalEntityInstanceWithItself() {
    // Arrange
    RentalEntity rental = new RentalEntity();
    rental.setId(UUID.randomUUID());
    rental.setStartDate(LocalDateTime.now());
    rental.setEndDate(LocalDateTime.now().plusDays(7));
    rental.setClient(new ClientEntity());
    rental.setParticipatingEmployee(new EmployeeEntity());
    rental.setVehicle(new VehicleEntity());

    // Act
    boolean result = rental.equals(rental);

    // Assert
    assertTrue(result);
}
@Test
void shouldReturnFalseWhenComparingRentalEntityInstancesWithDifferentIds() {
    // Arrange
    RentalEntity rental1 = new RentalEntity();
    rental1.setId(UUID.randomUUID());
    rental1.setStartDate(LocalDateTime.now());
    rental1.setEndDate(LocalDateTime.now().plusDays(7));
    rental1.setClient(new ClientEntity());
    rental1.setParticipatingEmployee(new EmployeeEntity());
    rental1.setVehicle(new VehicleEntity());

    RentalEntity rental2 = new RentalEntity();
    rental2.setId(UUID.randomUUID());
    rental2.setStartDate(rental1.getStartDate());
    rental2.setEndDate(rental1.getEndDate());
    rental2.setClient(rental1.getClient());
    rental2.setParticipatingEmployee(rental1.getParticipatingEmployee());
    rental2.setVehicle(rental1.getVehicle());

    // Act
    boolean result = rental1.equals(rental2);

    // Assert
    assertFalse(result);
}
@Test
void shouldReturnSameHashCodeForTwoRentalEntitiesWithIdenticalProperties() {
    // Arrange
    UUID id = UUID.randomUUID();
    LocalDateTime startDate = LocalDateTime.now();
    LocalDateTime endDate = LocalDateTime.now().plusDays(7);
    ClientEntity client = new ClientEntity();
    EmployeeEntity employee = new EmployeeEntity();
    VehicleEntity vehicle = new VehicleEntity();

    RentalEntity rental1 = new RentalEntity();
    rental1.setId(id);
    rental1.setStartDate(startDate);
    rental1.setEndDate(endDate);
    rental1.setClient(client);
    rental1.setParticipatingEmployee(employee);
    rental1.setVehicle(vehicle);

    RentalEntity rental2 = new RentalEntity();
    rental2.setId(id);
    rental2.setStartDate(startDate);
    rental2.setEndDate(endDate);
    rental2.setClient(client);
    rental2.setParticipatingEmployee(employee);
    rental2.setVehicle(vehicle);

    // Act
    int hashCode1 = rental1.hashCode();
    int hashCode2 = rental2.hashCode();

    // Assert
    assertEquals(hashCode1, hashCode2);
}
@Test
void shouldReturnHashCodeWhenSomePropertiesAreNull() {
    // Arrange
    RentalEntity rental = new RentalEntity();
    rental.setId(UUID.randomUUID());
    rental.setStartDate(null);
    rental.setEndDate(null);
    rental.setClient(null);
    rental.setParticipatingEmployee(null);
    rental.setVehicle(null);

    // Act
    int hashCode = rental.hashCode();

    // Assert
    assertNotEquals(0, hashCode);
}
}
