package psk.bio.car.rental.infrastructure.data.rentals;

import org.springframework.data.jpa.repository.JpaRepository;
import psk.bio.car.rental.application.rental.RentalProjection;
import psk.bio.car.rental.application.rental.RentalRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public interface RentalJpaRepository extends JpaRepository<RentalEntity, UUID>, RentalRepository {
    @Override
    default List<RentalProjection> findByClient(final String clientId) {
        return findByClientUserId(UUID.fromString(clientId)).stream()
                .map(RentalProjection.class::cast)
                .toList();
    }

    @Override
    default List<RentalProjection> findByEmployee(final String employeeId) {
        return findByParticipatingEmployeeUserId(UUID.fromString(employeeId)).stream()
                .map(RentalProjection.class::cast)
                .toList();
    };

    @Override
    default Optional<RentalProjection> findById(final String id) {
        return findById(UUID.fromString(id))
                .map(RentalProjection.class::cast);
    }

    List<RentalEntity> findByClientUserId(UUID clientId);

    List<RentalEntity> findByParticipatingEmployeeUserId(UUID employeeId);
}
