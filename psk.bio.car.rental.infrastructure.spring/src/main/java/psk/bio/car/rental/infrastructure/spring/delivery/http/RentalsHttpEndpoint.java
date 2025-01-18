package psk.bio.car.rental.infrastructure.spring.delivery.http;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psk.bio.car.rental.api.rentals.CreateVehicleRentalRequest;
import psk.bio.car.rental.application.rental.RentalService;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/rentals")
@RestController
public class RentalsHttpEndpoint {
    private final RentalService rentalService;

    @PostMapping
    public UUID createdVehicleRental(final @RequestBody @Valid CreateVehicleRentalRequest request) {
        return rentalService.rentVehicle(request.getVehicleId(), request.getClientId(), request.getNumberOfDays());
    }
}
