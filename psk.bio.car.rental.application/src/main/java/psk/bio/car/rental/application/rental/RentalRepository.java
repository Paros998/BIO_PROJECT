package psk.bio.car.rental.application.rental;

import java.util.List;
import java.util.Optional;

public interface RentalRepository {
    List<RentalProjection> findByClient(String clientId);

    List<RentalProjection> findByEmployee(String employeeId);

    Optional<RentalProjection> findById(String id);
}
