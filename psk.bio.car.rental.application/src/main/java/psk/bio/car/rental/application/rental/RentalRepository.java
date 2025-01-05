package psk.bio.car.rental.application.rental;

import java.util.List;
import java.util.Optional;

public interface RentalRepository {
    List<Rental> findByClient(String clientId);

    List<Rental> findByEmployee(String employeeId);

    Optional<Rental> findById(String id);
}
