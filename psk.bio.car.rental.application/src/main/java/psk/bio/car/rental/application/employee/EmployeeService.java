package psk.bio.car.rental.application.employee;

import lombok.NonNull;
import psk.bio.car.rental.api.common.paging.PageRequest;
import psk.bio.car.rental.api.common.paging.PageResponse;
import psk.bio.car.rental.api.employees.EmployeeModel;

import java.util.UUID;

public interface EmployeeService {
    void finishFirstLogin(@NonNull UUID employeeId, @NonNull String newPassword);

    PageResponse<EmployeeModel> fetchEmployees(@NonNull PageRequest pageRequest);

    void setEmployeeActiveState(@NonNull UUID employeeId, boolean newState);

    void returnVehicleAndSentItToRepairs(@NonNull UUID rentalId, @NonNull UUID employeeId);
}
