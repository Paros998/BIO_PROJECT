package psk.bio.car.rental.application.admin;

import psk.bio.car.rental.api.employees.CreateEmployeeRequest;

import java.util.UUID;

public interface AdminService {
    UUID addEmployee(CreateEmployeeRequest request);
}
