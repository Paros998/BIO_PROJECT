package psk.bio.car.rental.infrastructure.data.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import psk.bio.car.rental.api.clients.ClientModel;
import psk.bio.car.rental.api.clients.ClientRentedVehicles;
import psk.bio.car.rental.api.common.paging.PageRequest;
import psk.bio.car.rental.api.common.paging.PageResponse;
import psk.bio.car.rental.api.security.FinishRegisterRequest;
import psk.bio.car.rental.api.vehicles.RentedVehicle;
import psk.bio.car.rental.application.payments.PaymentStatus;
import psk.bio.car.rental.application.rental.RentalRepository;
import psk.bio.car.rental.application.rental.RentalState;
import psk.bio.car.rental.application.security.UserContextValidator;
import psk.bio.car.rental.application.security.UserRole;
import psk.bio.car.rental.application.user.ClientService;
import psk.bio.car.rental.application.user.UserProjection;
import psk.bio.car.rental.application.user.UserRepository;
import psk.bio.car.rental.infrastructure.data.client.ClientEntity;
import psk.bio.car.rental.infrastructure.data.client.ClientJpaRepository;
import psk.bio.car.rental.infrastructure.data.common.paging.PageMapper;
import psk.bio.car.rental.infrastructure.data.common.paging.SpringPageRequest;
import psk.bio.car.rental.infrastructure.data.common.paging.SpringPageResponse;
import psk.bio.car.rental.infrastructure.data.mappers.VehicleMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;
    private final ClientJpaRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserContextValidator userContextValidator;

    @Override
    @Transactional
    public UUID registerNewClient(final @NonNull String email, final @NonNull String password) {
        final Optional<UserProjection> user = userRepository.findByUsername(email);
        if (user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Email already registered.");
        }

        final ClientEntity client = ClientEntity.builder()
                .role(UserRole.CLIENT)
                .email(email)
                .password(passwordEncoder.encode(password))
                .enabled(Boolean.TRUE)
                .firstLoginDone(Boolean.FALSE)
                .build();

        return clientRepository.save(client).getUserId();
    }

    @Override
    @Transactional
    public void finishRegistration(final @NonNull UUID userId, final @NonNull FinishRegisterRequest request) {
        userContextValidator.checkUserPerformingAction(userId);
        final Optional<ClientEntity> clientBox = clientRepository.findById(userId);

        if (clientBox.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        final var client = clientBox.get();
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setPhoneNumber(request.getPhoneNumber());
        client.setNationalId(request.getNationalId());
        client.setFirstLoginDone(Boolean.TRUE);
        clientRepository.save(client);
    }

    @Override
    public PageResponse<ClientModel> fetchClients(final @NonNull PageRequest pageRequest) {
        final SpringPageRequest springPageRequest = PageMapper.toSpringPageRequest(pageRequest);
        final SpringPageResponse<ClientEntity> clients
                = new SpringPageResponse<>(clientRepository.findAll(springPageRequest.getRequest(ClientEntity.class)));
        return PageMapper.toPageResponse(clients, this::toClientModel);
    }

    @Override
    @Transactional
    public void setClientActiveState(final @NonNull UUID clientId, final boolean newState) {
        final ClientEntity client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
        client.setEnabled(newState);
        clientRepository.save(client);
    }

    @Override
    public ClientRentedVehicles getRentedVehicles(final @NonNull UUID clientId) {
        final ClientEntity client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        List<RentedVehicle> rentedVehicles = rentalRepository.findByClient(clientId.toString()).stream()
                .map(rental -> VehicleMapper.toRentedVehicle(rental.getVehicle(), rental))
                .toList();

        return ClientRentedVehicles.builder()
                .client(toClientModel(client))
                .vehicles(rentedVehicles)
                .build();
    }

    private ClientModel toClientModel(final @NonNull ClientEntity client) {
        return ClientModel.builder()
                .userId(client.getUserId())
                .email(client.getEmail())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .phoneNumber(client.getPhoneNumber())
                .nationalId(client.getNationalId())
                .isActive(client.isActive())
                .firstLoginDone(client.getFirstLoginDone())
                .rentedCarsCount((int) client.getRentedVehicles().stream()
                        .filter(rentalEntity -> rentalEntity.getState().equals(RentalState.ACTIVE))
                        .count())
                .duePaymentsCount((int) client.getClientPayments().stream()
                        .filter(paymentEntity -> paymentEntity.getStatus().equals(PaymentStatus.PENDING))
                        .count())
                .build();
    }
}
