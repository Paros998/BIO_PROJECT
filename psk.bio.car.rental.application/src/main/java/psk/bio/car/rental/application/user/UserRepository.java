package psk.bio.car.rental.application.user;

import org.springframework.security.core.userdetails.UserDetailsService;
import psk.bio.car.rental.application.security.UserProjection;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends UserDetailsService {
    Optional<UserProjection> findByUsername(String username);

    Collection<UserProjection> findAllUsers();

    Optional<UserProjection> findById(String id);
}