package psk.bio.car.rental.infrastructure.spring.delivery.http;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import psk.bio.car.rental.api.employees.vehicles.management.FinishRepairsRequest;
import psk.bio.car.rental.api.employees.vehicles.management.InsureVehicleRequest;
import psk.bio.car.rental.api.employees.vehicles.management.MakeVehicleReadyToRentRequest;
import psk.bio.car.rental.api.employees.vehicles.management.SendVehicleToRepairsRequest;
import psk.bio.car.rental.infrastructure.data.services.EmployeeServiceImpl;

@RequiredArgsConstructor
@RequestMapping("/api/employees/manage-vehicles")
@RestController
public class EmployeeVehicleManagementHttpEndpoint {
    private final EmployeeServiceImpl employeeService;

    @PutMapping("/finish-repairs")
    public void finishVehicleRepairs(final @Valid @RequestBody FinishRepairsRequest request) {
        if (request.getChargeCustomer()) {
            employeeService.finishRepairsAndChargeLastCustomerThatRentedCar(request.getEmployeeId(), request.getVehicleId(),
                    request.getTotalCost(), request.getBankAccountNumber(), request.getDueDate());
        } else {
            employeeService.finishRepairsAndChargeCompany(request.getEmployeeId(), request.getVehicleId(),
                    request.getTotalCost(), request.getBankAccountNumber(), request.getDueDate());
        }
    }

    @PutMapping("/insure-vehicle")
    public void insureVehicle(final @Valid @RequestBody InsureVehicleRequest request) {
        employeeService.insureVehicleAndMakeInsured(request.getEmployeeId(), request.getVehicleId(), request.getInsuranceId(),
                request.getBankAccountNumber(), request.getInsuranceCost(), request.getDueDate());
    }

    @PutMapping("/send-to-repairs")
    public void sendVehicleToRepairs(final @Valid @RequestBody SendVehicleToRepairsRequest request) {
        employeeService.sendInsuredOrJustReturnedVehicleToRepairs(request.getEmployeeId(), request.getVehicleId());
    }

    @PutMapping("/make-ready-to-rent")
    public void makeReadyToRent(final @Valid @RequestBody MakeVehicleReadyToRentRequest request) {
        employeeService.makeInsuredVehicleReadyToRent(request.getEmployeeId(), request.getVehicleId());
    }
}
