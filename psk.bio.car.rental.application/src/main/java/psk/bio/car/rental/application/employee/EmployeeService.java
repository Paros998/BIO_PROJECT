package psk.bio.car.rental.application.employee;

import psk.bio.car.rental.api.common.paging.PageRequest;
import psk.bio.car.rental.api.common.paging.PageResponse;
import psk.bio.car.rental.api.employees.EmployeeModel;

import java.util.UUID;

public interface EmployeeService {
    void finishFirstLogin(UUID employeeId, String newPassword);

    PageResponse<EmployeeModel> fetchEmployees(PageRequest pageRequest);

    void setEmployeeActiveState(UUID employeeId, boolean newState);
}
