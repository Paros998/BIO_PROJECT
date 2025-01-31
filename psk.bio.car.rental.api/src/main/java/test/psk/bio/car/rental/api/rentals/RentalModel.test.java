package test.psk.bio.car.rental.api.rentals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import psk.bio.car.rental.api.rentals.RentalModel;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RentalModelTest {
    @Test
void shouldCreateRentalModelWithAllFieldsPopulated() {
    UUID rentalId = UUID.randomUUID();
    UUID clientId = UUID.randomUUID();
    UUID vehicleId = UUID.randomUUID();
    UUID approvingEmployeeId = UUID.randomUUID();
    LocalDateTime startDate = LocalDateTime.now();
    LocalDateTime endDate = startDate.plusDays(7);
    Boolean paymentsFeesPaid = true;

    RentalModel rentalModel = RentalModel.builder()
            .rentalId(rentalId)
            .clientId(clientId)
            .vehicleId(vehicleId)
            .approvingEmployeeId(approvingEmployeeId)
            .startDate(startDate)
            .endDate(endDate)
            .paymentsFeesPaid(paymentsFeesPaid)
            .build();

    assertNotNull(rentalModel);
    assertEquals(rentalId, rentalModel.getRentalId());
    assertEquals(clientId, rentalModel.getClientId());
    assertEquals(vehicleId, rentalModel.getVehicleId());
    assertEquals(approvingEmployeeId, rentalModel.getApprovingEmployeeId());
    assertEquals(startDate, rentalModel.getStartDate());
    assertEquals(endDate, rentalModel.getEndDate());
    assertEquals(paymentsFeesPaid, rentalModel.getPaymentsFeesPaid());
}
@Test
void shouldReturnTrueForEqualsMethodWhenComparingTwoIdenticalRentalModelInstances() {
    UUID rentalId = UUID.randomUUID();
    UUID clientId = UUID.randomUUID();
    UUID vehicleId = UUID.randomUUID();
    UUID approvingEmployeeId = UUID.randomUUID();
    LocalDateTime startDate = LocalDateTime.now();
    LocalDateTime endDate = startDate.plusDays(7);
    Boolean paymentsFeesPaid = true;

    RentalModel rentalModel1 = RentalModel.builder()
            .rentalId(rentalId)
            .clientId(clientId)
            .vehicleId(vehicleId)
            .approvingEmployeeId(approvingEmployeeId)
            .startDate(startDate)
            .endDate(endDate)
            .paymentsFeesPaid(paymentsFeesPaid)
            .build();

    RentalModel rentalModel2 = RentalModel.builder()
            .rentalId(rentalId)
            .clientId(clientId)
            .vehicleId(vehicleId)
            .approvingEmployeeId(approvingEmployeeId)
            .startDate(startDate)
            .endDate(endDate)
            .paymentsFeesPaid(paymentsFeesPaid)
            .build();

    assertEquals(rentalModel1, rentalModel2);
}
@Test
void shouldCorrectlyHandleLocalDateTimeObjectsForStartDateAndEndDate() {
    LocalDateTime startDate = LocalDateTime.of(2023, 5, 1, 10, 0);
    LocalDateTime endDate = LocalDateTime.of(2023, 5, 8, 10, 0);

    RentalModel rentalModel = RentalModel.builder()
            .rentalId(UUID.randomUUID())
            .clientId(UUID.randomUUID())
            .vehicleId(UUID.randomUUID())
            .approvingEmployeeId(UUID.randomUUID())
            .startDate(startDate)
            .endDate(endDate)
            .paymentsFeesPaid(true)
            .build();

    assertNotNull(rentalModel.getStartDate());
    assertNotNull(rentalModel.getEndDate());
    assertEquals(startDate, rentalModel.getStartDate());
    assertEquals(endDate, rentalModel.getEndDate());
    assertTrue(rentalModel.getStartDate().isBefore(rentalModel.getEndDate()));
}
@Test
void shouldGenerateDifferentHashCodesForDifferentRentalModelInstances() {
    RentalModel rentalModel1 = RentalModel.builder()
            .rentalId(UUID.randomUUID())
            .clientId(UUID.randomUUID())
            .vehicleId(UUID.randomUUID())
            .approvingEmployeeId(UUID.randomUUID())
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().plusDays(7))
            .paymentsFeesPaid(true)
            .build();

    RentalModel rentalModel2 = RentalModel.builder()
            .rentalId(UUID.randomUUID())
            .clientId(UUID.randomUUID())
            .vehicleId(UUID.randomUUID())
            .approvingEmployeeId(UUID.randomUUID())
            .startDate(LocalDateTime.now().plusDays(1))
            .endDate(LocalDateTime.now().plusDays(8))
            .paymentsFeesPaid(false)
            .build();

    assertNotEquals(rentalModel1.hashCode(), rentalModel2.hashCode());
}
}
