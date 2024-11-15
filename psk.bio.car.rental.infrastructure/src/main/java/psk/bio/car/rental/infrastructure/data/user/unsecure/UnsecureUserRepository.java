package psk.bio.car.rental.infrastructure.data.user.unsecure;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import psk.bio.car.rental.application.profiles.ApplicationProfile;
import psk.bio.car.rental.application.user.UserProjection;
import psk.bio.car.rental.application.user.UserRepository;
import psk.bio.car.rental.infrastructure.data.user.UserEntity;

import java.util.List;
import java.util.Optional;

@Profile(ApplicationProfile.UNSECURE)
@SuppressWarnings("unused")
@NoArgsConstructor
public class UnsecureUserRepository implements UserRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<UserProjection> findAllUsers() {
        return em.createQuery("select u from users u", UserEntity.class)
                .getResultList()
                .stream()
                .map(UserProjection.class::cast)
                .toList();
    }

    @Override
    public Optional<UserProjection> findById(final String id) {
        return Optional.ofNullable(em.find(UserEntity.class, id))
                .map(UserProjection.class::cast);
    }

    @Override
    public Optional<UserProjection> findByUsername(final String username) {
        return findByEmail(username).map(UserProjection.class::cast);
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    /*
    * Sql Injection
    * */
    private Optional<UserEntity> findByEmail(final String email) {
        try {
            return Optional.of((UserEntity)
                    em.createNativeQuery(String.format("select *, 0 as clazz_, 0 as employee_identifier"
                                            + " from users u where u.email = '%s'", email), UserEntity.class)
                            .getSingleResult()
            );
        } catch (final NoResultException e) {
            return Optional.empty();
        }
    }
}
