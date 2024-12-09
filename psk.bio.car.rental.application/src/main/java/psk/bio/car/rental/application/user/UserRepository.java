package psk.bio.car.rental.application.user;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends UserDetailsService {
    Optional<UserProjection> findByUsername(String username);

    List<UserProjection> findAllUsers();

    Optional<UserProjection> findById(String id);
}
