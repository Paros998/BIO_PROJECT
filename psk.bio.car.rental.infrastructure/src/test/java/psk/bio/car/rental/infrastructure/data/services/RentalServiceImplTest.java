package psk.bio.car.rental.infrastructure.data.services;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import psk.bio.car.rental.application.payments.CompanyFinancialConfiguration;
import psk.bio.car.rental.application.security.UserContextValidator;
import psk.bio.car.rental.application.user.UserRepository;
import psk.bio.car.rental.infrastructure.data.client.ClientEntity;
import psk.bio.car.rental.infrastructure.data.client.ClientJpaRepository;
import psk.bio.car.rental.infrastructure.data.payments.PaymentEntity;
import psk.bio.car.rental.infrastructure.data.payments.PaymentJpaRepository;
import psk.bio.car.rental.infrastructure.data.rentals.RentalEntity;
import psk.bio.car.rental.infrastructure.data.rentals.RentalJpaRepository;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleEntity;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceImplTest {

    @Mock
    private RentalJpaRepository rentalRepository;

    @Mock
    private VehicleServiceImpl vehicleService;

    @Mock
    private ClientServiceImpl clientService;

    @Mock
    private PaymentServiceImpl paymentService;

    @Mock
    private CompanyFinancialConfiguration companyFinancialConfiguration;

    @Mock
    private PaymentJpaRepository paymentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ClientJpaRepository clientRepository;

    @Mock
    private UserContextValidator userContextValidator;

    @InjectMocks
    private RentalServiceImpl rentalService;

    @Test
    @Disabled // FIXME
    void shouldRentVehicleSuccessfully() {
        // Arrange
        UUID vehicleId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        int numberOfDays = 5;
        VehicleEntity vehicle = new VehicleEntity();
        vehicle.setRentPerDayPrice(new BigDecimal("100.00"));
        ClientEntity client = mock(ClientEntity.class);
        RentalEntity rentalEntity = new RentalEntity();
        rentalEntity.setId(UUID.randomUUID());
        PaymentEntity paymentEntity = new PaymentEntity();

        when(vehicleService.findReadyToRentVehicle(vehicleId)).thenReturn(vehicle);
        when(userRepository.findById(String.valueOf(clientId))).thenReturn(Optional.of(client));
        when(rentalRepository.save(any(RentalEntity.class))).thenReturn(rentalEntity);
        when(paymentRepository.save(any(PaymentEntity.class))).thenReturn(paymentEntity);
        when(companyFinancialConfiguration.getCompanyBankAccountNumber()).thenReturn("123456789123123123");

        // Act
        UUID rentalId = rentalService.rentVehicle(vehicleId, clientId, numberOfDays);

        // Assert
        assertNotNull(rentalId);
        verify(userContextValidator).checkUserPerformingAction(clientId);
        verify(vehicleService).findReadyToRentVehicle(vehicleId);
        verify(userRepository).findById(String.valueOf(clientId));
        verify(paymentRepository).save(any(PaymentEntity.class));
        verify(rentalRepository).save(any(RentalEntity.class));
    }

    @Test
    @Disabled // FIXME
    void shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        UUID vehicleId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        int numberOfDays = 5;

        when(vehicleService.findReadyToRentVehicle(vehicleId)).thenReturn(new VehicleEntity());
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());
        when(clientService.findById(clientId)).thenAnswer(answer -> clientRepository.findById(answer.getArgument(0)));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            rentalService.rentVehicle(vehicleId, clientId, numberOfDays);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void shouldThrowExceptionWhenVehicleNotFound() {
        // Arrange
        UUID vehicleId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        int numberOfDays = 5;

        when(vehicleService.findReadyToRentVehicle(vehicleId)).thenThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            rentalService.rentVehicle(vehicleId, clientId, numberOfDays);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void shouldThrowExceptionForInvalidNumberOfDays() {
        // Arrange
        UUID vehicleId = UUID.randomUUID();
        UUID clientId = UUID.randomUUID();
        int numberOfDays = -1;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            rentalService.rentVehicle(vehicleId, clientId, numberOfDays);
        });

        assertEquals("Number of days must be greater than 0", exception.getMessage());
    }
}