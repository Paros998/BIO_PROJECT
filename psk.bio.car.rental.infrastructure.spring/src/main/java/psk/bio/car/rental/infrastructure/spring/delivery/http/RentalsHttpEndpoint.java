package psk.bio.car.rental.infrastructure.spring.delivery.http;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import psk.bio.car.rental.api.rentals.CreateVehicleRentalRequest;
import psk.bio.car.rental.api.rentals.FinishVehicleRentalRequest;
import psk.bio.car.rental.infrastructure.data.services.EmployeeServiceImpl;
import psk.bio.car.rental.infrastructure.data.services.RentalServiceImpl;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/rentals")
@RestController
public class RentalsHttpEndpoint {
    private final RentalServiceImpl rentalService;
    private final EmployeeServiceImpl employeeService;

    @PostMapping
    public UUID createdVehicleRental(final @RequestBody @Valid CreateVehicleRentalRequest request) {
        return rentalService.rentVehicle(request.getVehicleId(), request.getClientId(), request.getNumberOfDays());
    }

    @PutMapping("/finish-rental/make-ready-to-rent")
    public void finishVehicleRentalAndMakeReadyToRent(final @RequestBody @Valid FinishVehicleRentalRequest request) {
        employeeService.returnVehicleAndMakeReadyToRent(request.getRentalId(), request.getEmployeeId());
    }

    @PutMapping("/finish-rental/send-to-repairs")
    public void finishVehicleRentalAndSendToRepairs(final @RequestBody @Valid FinishVehicleRentalRequest request) {
        employeeService.returnVehicleAndSentItToRepairs(request.getRentalId(), request.getEmployeeId());
    }
}
