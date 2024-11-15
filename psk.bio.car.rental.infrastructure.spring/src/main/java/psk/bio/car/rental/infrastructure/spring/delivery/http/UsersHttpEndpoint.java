package psk.bio.car.rental.infrastructure.spring.delivery.http;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psk.bio.car.rental.application.user.UserProjection;
import psk.bio.car.rental.application.user.UserRepository;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UsersHttpEndpoint {
    private final UserRepository userRepository;

    @GetMapping
    public List<UserProjection> getUsers() {
        return userRepository.findAllUsers();
    }
}
