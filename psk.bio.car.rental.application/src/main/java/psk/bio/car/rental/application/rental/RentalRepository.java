package psk.bio.car.rental.application.rental;

import java.util.List;

public interface RentalRepository {
    List<Rental> findByClient(String clientId);

    List<Rental> findByEmployee(String employeeId);
}
