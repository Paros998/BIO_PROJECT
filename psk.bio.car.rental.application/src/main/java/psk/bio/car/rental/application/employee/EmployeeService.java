package psk.bio.car.rental.application.employee;

import lombok.NonNull;
import psk.bio.car.rental.api.common.paging.PageRequest;
import psk.bio.car.rental.api.common.paging.PageResponse;
import psk.bio.car.rental.api.employees.EmployeeModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface EmployeeService {
    void finishFirstLogin(@NonNull UUID employeeId, @NonNull String newPassword);

    PageResponse<EmployeeModel> fetchEmployees(@NonNull PageRequest pageRequest);

    void setEmployeeActiveState(@NonNull UUID employeeId, boolean newState);

    void finishRepairsAndChargeCompany(@NonNull UUID employeeId, @NonNull UUID vehicleId, @NonNull BigDecimal totalCost,
                                       @NonNull String bankAccountNumber, @NonNull LocalDate dueDate);

    void finishRepairsAndChargeLastCustomerThatRentedCar(@NonNull UUID employeeId, @NonNull UUID vehicleId, @NonNull BigDecimal totalCost,
                                                         @NonNull String bankAccountNumber, @NonNull LocalDate dueDate);

    void returnVehicleAndMakeReadyToRent(@NonNull UUID rentalId, @NonNull UUID employeeId);

    void returnVehicleAndSentItToRepairs(@NonNull UUID rentalId, @NonNull UUID employeeId);

    void insureVehicleAndMakeInsured(@NonNull UUID vehicleId, @NonNull UUID employeeId, @NonNull String insuranceId,
                                     @NonNull String bankAccountNumber, @NonNull BigDecimal insuranceCost, @NonNull LocalDate dueDate);

    void sendInsuredOrJustReturnedVehicleToRepairs(@NonNull UUID vehicleId, @NonNull UUID employeeId);
}
