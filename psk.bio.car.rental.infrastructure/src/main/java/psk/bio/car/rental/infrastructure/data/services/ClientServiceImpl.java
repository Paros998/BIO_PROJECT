package psk.bio.car.rental.infrastructure.data.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import psk.bio.car.rental.api.security.FinishRegisterRequest;
import psk.bio.car.rental.application.user.ClientService;
import psk.bio.car.rental.application.user.UserProjection;
import psk.bio.car.rental.application.user.UserRepository;
import psk.bio.car.rental.infrastructure.data.client.ClientEntity;
import psk.bio.car.rental.infrastructure.data.client.ClientJpaRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final UserRepository userRepository;
    private final ClientJpaRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UUID registerNewClient(final @NonNull String email, final @NonNull String password) {
        final Optional<UserProjection> user = userRepository.findByUsername(email);
        if (user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Email already registered.");
        }

        final ClientEntity client = new ClientEntity();

        client.setEmail(email);
        client.setPassword(passwordEncoder.encode(password));
        client.setEnabled(Boolean.TRUE);
        client.setFirstLoginDone(Boolean.FALSE);

        return clientRepository.save(client).getUserId();
    }

    @Override
    @Transactional
    public void finishRegistration(final UUID userId, final @NonNull FinishRegisterRequest request) {
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
}
