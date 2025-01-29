package psk.bio.car.rental.infrastructure.spring.delivery.http;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import psk.bio.car.rental.api.security.FinishRegisterRequest;
import psk.bio.car.rental.api.security.RegisterRequest;
import psk.bio.car.rental.api.users.UserModel;
import psk.bio.car.rental.api.users.UserRole;
import psk.bio.car.rental.application.user.UserProjection;
import psk.bio.car.rental.application.user.UserRepository;
import psk.bio.car.rental.infrastructure.data.services.ClientServiceImpl;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UsersHttpEndpoint {
    private final UserRepository userRepository;
    private final ClientServiceImpl clientService;

    @GetMapping("/{userId}")
    public UserModel findUser(final @PathVariable("userId") UUID userId) {
        return userRepository.findById(String.valueOf(userId))
                .map(user -> UserModel.builder()
                        .userId(user.getUserId())
                        .username(user.getUsername())
                        .enabled(user.isEnabled())
                        .firstLogin(user.isFirstLogin())
                        .role(UserRole.valueOf(user.getRole().name()))
                        .build())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public List<UserProjection> getUsers() {
        return userRepository.findAllUsers();
    }

    // --------------------------------------

    @PostMapping("/register")
    public UUID register(final @Valid @RequestBody RegisterRequest request) {
        return clientService.registerNewClient(request.getUsername(), request.getPassword());
    }

    @PostMapping("/finish-register/{userId}")
    public void finishRegister(final @PathVariable("userId") UUID userId, final @Valid @RequestBody FinishRegisterRequest request) {
        clientService.finishRegistration(userId, request);
    }
}
