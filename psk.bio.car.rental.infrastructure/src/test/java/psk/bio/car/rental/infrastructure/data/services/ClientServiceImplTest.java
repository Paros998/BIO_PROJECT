package psk.bio.car.rental.infrastructure.data.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import psk.bio.car.rental.api.security.FinishRegisterRequest;
import psk.bio.car.rental.application.rental.RentalRepository;
import psk.bio.car.rental.application.security.UserContextValidator;
import psk.bio.car.rental.application.security.UserRole;
import psk.bio.car.rental.application.user.UserRepository;
import psk.bio.car.rental.infrastructure.data.client.ClientEntity;
import psk.bio.car.rental.infrastructure.data.client.ClientJpaRepository;
import psk.bio.car.rental.infrastructure.data.rentals.RentalEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ClientJpaRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserContextValidator userContextValidator;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private ClientServiceImpl clientModelMapper;


    @Test
    void shouldSuccessfullyRegisterNewClientWithValidEmailAndPassword() {
        // Given
        String email = "test@example.com";
        String password = "password123";
        UUID expectedUserId = UUID.randomUUID();
        ClientEntity expectedClientEntity = ClientEntity.builder()
                .role(UserRole.CLIENT)
                .email(email)
                .password("encodedPassword")
                .enabled(true)
                .firstLoginDone(false)
                .userId(expectedUserId) // Ustawienie userId
                .build();

        when(userRepository.findByUsername(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(clientRepository.save(any(ClientEntity.class))).thenReturn(expectedClientEntity);

        // When
        UUID result = clientService.registerNewClient(email, password);

        // Then
        assertEquals(expectedUserId, result);
        verify(userRepository).findByUsername(email);
        verify(passwordEncoder).encode(password);
        verify(clientRepository).save(any(ClientEntity.class));
    }


    @Test
    void shouldSetUserRoleToClientForNewRegistrations() {
        // Given
        String email = "newclient@example.com";
        String password = "password123";
        UUID expectedUserId = UUID.randomUUID();
        ClientEntity expectedClientEntity = ClientEntity.builder()
                .role(UserRole.CLIENT)
                .email(email)
                .password("encodedPassword")
                .enabled(true)
                .firstLoginDone(false)
                .userId(expectedUserId) // Ustawienie userId
                .build();

        when(userRepository.findByUsername(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(clientRepository.save(any(ClientEntity.class))).thenAnswer(invocation -> {
            ClientEntity client = invocation.getArgument(0);
            client.setUserId(expectedUserId); // Ustawienie userId w zwracanym obiekcie
            return client;
        });

        // When
        UUID result = clientService.registerNewClient(email, password);

        // Then
        assertEquals(expectedUserId, result);
        verify(clientRepository).save(argThat(client ->
                client.getRole() == UserRole.CLIENT &&
                        client.getEmail().equals(email) &&
                        client.getPassword().equals("encodedPassword") &&
                        client.isEnabled() &&
                        !client.getFirstLoginDone()
        ));
    }


    @Test
    void shouldSetEnabledToTrueForNewClientRegistrations() {
        // Given
        String email = "newclient@example.com";
        String password = "password123";
        UUID expectedUserId = UUID.randomUUID();
        ClientEntity expectedClientEntity = ClientEntity.builder()
                .role(UserRole.CLIENT)
                .email(email)
                .password("encodedPassword")
                .enabled(true)
                .firstLoginDone(false)
                .userId(expectedUserId) // Ustawienie userId bezpośrednio w obiekcie
                .build();

        when(userRepository.findByUsername(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(clientRepository.save(any(ClientEntity.class))).thenAnswer(invocation -> {
            ClientEntity client = invocation.getArgument(0);
            client.setUserId(expectedUserId); // Ustawienie userId w zwracanym obiekcie
            return client;
        });

        // When
        UUID result = clientService.registerNewClient(email, password);

        // Then
        assertEquals(expectedUserId, result);
        verify(clientRepository).save(argThat(client ->
                client.isEnabled() &&
                        client.getEmail().equals(email) &&
                        client.getPassword().equals("encodedPassword") &&
                        client.getRole() == UserRole.CLIENT &&
                        !client.getFirstLoginDone()
        ));
    }


    @Test
    void shouldVerifySavedClientEntityMatchesInputData() {
        // Given
        String email = "test@example.com";
        String password = "password123";
        UUID expectedUserId = UUID.randomUUID();
        String encodedPassword = "encodedPassword";

        ClientEntity expectedClientEntity = ClientEntity.builder()
                .role(UserRole.CLIENT)
                .email(email)
                .password(encodedPassword)
                .enabled(true)
                .firstLoginDone(false)
                .userId(expectedUserId) // Dodaj userId
                .build();

        when(userRepository.findByUsername(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(clientRepository.save(any(ClientEntity.class))).thenAnswer(invocation -> {
            ClientEntity client = invocation.getArgument(0);
            client.setUserId(expectedUserId); // Ustawiamy UUID
            return client;
        });

        // When
        UUID result = clientService.registerNewClient(email, password);

        // Then
        assertEquals(expectedUserId, result);
        verify(clientRepository).save(argThat(client ->
                UserRole.CLIENT.equals(client.getRole()) &&
                        email.equals(client.getEmail()) &&
                        encodedPassword.equals(client.getPassword()) &&
                        client.isEnabled() &&
                        !client.getFirstLoginDone()
        ));
    }


    @Test
    void shouldSetFirstLoginDoneToFalseForNewClientRegistrations() {
        // Given
        String email = "newclient@example.com";
        String password = "password123";
        UUID expectedUserId = UUID.randomUUID();
        ClientEntity expectedClientEntity = ClientEntity.builder()
                .role(UserRole.CLIENT)
                .email(email)
                .password("encodedPassword")
                .enabled(true)
                .firstLoginDone(false)
                .userId(expectedUserId)
                .build();

        when(userRepository.findByUsername(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(clientRepository.save(any(ClientEntity.class))).thenAnswer(invocation -> {
            ClientEntity client = invocation.getArgument(0);
            client.setUserId(expectedUserId);
            return client;
        });

        // When
        UUID result = clientService.registerNewClient(email, password);

        // Then
        assertEquals(expectedUserId, result);
        verify(clientRepository).save(argThat(client ->
                client.getEmail().equals(email) &&
                        client.getPassword().equals("encodedPassword") &&
                        client.isEnabled() &&
                        !client.getFirstLoginDone() &&
                        client.getRole() == UserRole.CLIENT
        ));
    }

    @Test
    void shouldThrowResponseStatusExceptionWhenClientNotFound() {
        // Given
        UUID userId = UUID.randomUUID();
        FinishRegisterRequest request = new FinishRegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPhoneNumber("1234567890");
        request.setNationalId("ABC123");

        when(clientRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResponseStatusException.class, () -> clientService.finishRegistration(userId, request));
        verify(userContextValidator).checkUserPerformingAction(userId);
        verify(clientRepository).findById(userId);
        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    void shouldUpdateClientNationalIdCorrectly() {
        // Given
        UUID userId = UUID.randomUUID();
        String newNationalId = "ABC123456";
        FinishRegisterRequest request = new FinishRegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPhoneNumber("1234567890");
        request.setNationalId(newNationalId);

        ClientEntity existingClient = new ClientEntity();
        existingClient.setUserId(userId);
        existingClient.setNationalId("OldNationalId");

        when(clientRepository.findById(userId)).thenReturn(Optional.of(existingClient));
        when(clientRepository.save(any(ClientEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        clientService.finishRegistration(userId, request);

        // Then
        verify(userContextValidator).checkUserPerformingAction(userId);
        verify(clientRepository).findById(userId);
        verify(clientRepository).save(argThat(client ->
                client.getNationalId().equals(newNationalId) &&
                        client.getFirstName().equals("John") &&
                        client.getLastName().equals("Doe") &&
                        client.getPhoneNumber().equals("1234567890") &&
                        client.getFirstLoginDone()
        ));
    }

    @Test
    void shouldValidateUserContextBeforePerformingAction() {
        // Given
        UUID userId = UUID.randomUUID();
        FinishRegisterRequest request = new FinishRegisterRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPhoneNumber("1234567890");
        request.setNationalId("ABC123");

        doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN))
                .when(userContextValidator).checkUserPerformingAction(userId);

        // When & Then
        assertThrows(ResponseStatusException.class, () -> clientService.finishRegistration(userId, request));
        verify(userContextValidator).checkUserPerformingAction(userId);
        verifyNoInteractions(clientRepository);
    }

    @Test
    void shouldThrowResponseStatusExceptionWhenClientNotFoundInGetRentedVehicles() {
        // Given
        UUID clientId = UUID.randomUUID();
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // When & Then
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> clientService.getRentedVehicles(clientId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Client not found", exception.getReason());
        verify(clientRepository).findById(clientId);
        verifyNoInteractions(rentalRepository);
    }
    @Test
    void shouldHandleRentalWithNullVehicle() {
        // Given
        UUID clientId = UUID.randomUUID();
        ClientEntity client = new ClientEntity();
        client.setUserId(clientId);

        RentalEntity rentalWithNullVehicle = new RentalEntity();
        rentalWithNullVehicle.setVehicle(null);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(rentalRepository.findByClient(clientId.toString())).thenReturn(List.of(rentalWithNullVehicle));

        // When
        Exception exception = assertThrows(NullPointerException.class,
                () -> clientService.getRentedVehicles(clientId));

        // Then
        assertEquals("vehicle is marked non-null but is null", exception.getMessage());
        verify(clientRepository).findById(clientId);
        verify(rentalRepository).findByClient(clientId.toString());
    }


}