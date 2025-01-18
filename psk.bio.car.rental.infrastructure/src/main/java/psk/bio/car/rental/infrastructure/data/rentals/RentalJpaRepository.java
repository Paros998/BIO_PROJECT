package psk.bio.car.rental.infrastructure.data.rentals;

import org.springframework.data.jpa.repository.JpaRepository;
import psk.bio.car.rental.application.rental.Rental;
import psk.bio.car.rental.application.rental.RentalRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unused")
public interface RentalJpaRepository extends JpaRepository<RentalEntity, UUID>, RentalRepository {
    @Override
    default List<Rental> findByClient(final String clientId) {
        return findByClientUserId(UUID.fromString(clientId)).stream()
                .map(Rental.class::cast)
                .toList();
    }

    @Override
    default List<Rental> findByEmployee(final String employeeId) {
        return findByParticipatingEmployeeUserId(UUID.fromString(employeeId)).stream()
                .map(Rental.class::cast)
                .toList();
    }

    @Override
    default Optional<Rental> findById(final String id) {
        return findById(UUID.fromString(id))
                .map(Rental.class::cast);
    }

    List<RentalEntity> findByClientUserId(UUID clientId);

    List<RentalEntity> findByParticipatingEmployeeUserId(UUID employeeId);
}
