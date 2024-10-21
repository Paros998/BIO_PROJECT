package psk.bio.car.rental.infrastructure.data.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import psk.bio.car.rental.application.user.UserProjection;
import psk.bio.car.rental.application.user.UserRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID>, UserRepository {
    @Override
    default Optional<UserProjection> findByUsername(final String username) {
        return findByEmail(username).map(UserProjection.class::cast);
    }

    @Override
    default UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    default Collection<UserProjection> findAllUsers() {
        return findAll().stream()
                .map(UserProjection.class::cast)
                .toList();
    }

    @Override
    default Optional<UserProjection> findById(final String id) {
        return findById(UUID.fromString(id))
                .map(UserProjection.class::cast);
    }

    Optional<UserEntity> findByEmail(final String username);
}