package psk.bio.car.rental.spring.boot.standalone.starters.rentals;

import lombok.Data;
import lombok.NoArgsConstructor;
import psk.bio.car.rental.infrastructure.data.rentals.RentalEntity;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RentalsToAddConfig {
    List<RentalToAdd> rentals = new ArrayList<>();

    @Data
    @NoArgsConstructor
    public static class RentalToAdd {
        private String plate;
        private String clientEmail;
        private String employeeEmail;
        private RentalEntity rentalEntity;
    }
}
