package psk.bio.car.rental.infrastructure.data.user.unsecure;

import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import psk.bio.car.rental.application.profiles.ApplicationProfile;
import psk.bio.car.rental.infrastructure.data.user.UserEntity;
import psk.bio.car.rental.infrastructure.data.user.UserJpaRepository;

import java.util.Optional;
import java.util.UUID;

@Profile(ApplicationProfile.UNSECURE)
@SuppressWarnings("unused")
public interface UnsecureUserJpaRepository extends UserJpaRepository {

    @NonNull
    @Override
    @Query(nativeQuery = true, value = "select * from users u where u.userId = '?'")
    Optional<UserEntity> findById(@NonNull UUID id);

    @Override
    @Query(nativeQuery = true, value = "select * from users u where u.email = '?'")
    Optional<UserEntity> findByEmail(@NonNull String username);
}
