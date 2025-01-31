package psk.bio.car.rental.infrastructure.data.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import psk.bio.car.rental.api.common.paging.PageRequest;
import psk.bio.car.rental.api.common.paging.PageResponse;
import psk.bio.car.rental.api.employees.EmployeeModel;
import psk.bio.car.rental.application.employee.EmployeeService;
import psk.bio.car.rental.application.security.UserContextValidator;
import psk.bio.car.rental.application.vehicle.VehicleState;
import psk.bio.car.rental.infrastructure.data.admin.AdminEntity;
import psk.bio.car.rental.infrastructure.data.common.paging.PageMapper;
import psk.bio.car.rental.infrastructure.data.common.paging.SpringPageRequest;
import psk.bio.car.rental.infrastructure.data.common.paging.SpringPageResponse;
import psk.bio.car.rental.infrastructure.data.employee.EmployeeEntity;
import psk.bio.car.rental.infrastructure.data.employee.EmployeeJpaRepository;
import psk.bio.car.rental.infrastructure.data.rentals.RentalEntity;
import psk.bio.car.rental.infrastructure.data.vehicle.VehicleEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeJpaRepository employeeRepository;

    private final PasswordEncoder passwordEncoder;
    private final UserContextValidator userContextValidator;

    @Autowired
    private @Lazy PaymentServiceImpl paymentService;
    @Autowired
    private @Lazy RentalServiceImpl rentalService;
    @Autowired
    private @Lazy VehicleServiceImpl vehicleService;

    @Override
    @Transactional
    public void finishFirstLogin(final @NonNull UUID employeeId, final @NonNull String newPassword) {
        userContextValidator.checkUserPerformingAction(employeeId);

        final EmployeeEntity employee = getEmployee(employeeId);

        employee.setPassword(passwordEncoder.encode(newPassword));
        employee.setFirstLoginDone(Boolean.TRUE);
        employeeRepository.save(employee);
    }

    @Override
    public PageResponse<EmployeeModel> fetchEmployees(final @NonNull PageRequest pageRequest) {
        final SpringPageRequest springPageRequest = PageMapper.toSpringPageRequest(pageRequest);
        final SpringPageResponse<EmployeeEntity> employees =
                new SpringPageResponse<>(employeeRepository.findAll(springPageRequest.getRequest(EmployeeEntity.class)));
        return PageMapper.toPageResponse(employees, this::toEmployeeModel);
    }

    @Override
    @Transactional
    public void setEmployeeActiveState(final @NonNull UUID employeeId, final boolean newState) {
        final EmployeeEntity employee = getEmployee(employeeId);
        employee.setEnabled(newState);
        employeeRepository.save(employee);
    }

    @Transactional
    public void finishRepairsAndChargeCompany(final @NonNull UUID employeeId, final @NonNull UUID vehicleId,
                                              final @NonNull BigDecimal totalCost, final @NonNull String bankAccountNumber,
                                              final @NonNull LocalDate dueDate) {
        userContextValidator.checkUserPerformingAction(employeeId);
        final EmployeeEntity employee = getEmployee(employeeId);
        final VehicleEntity vehicle = vehicleService.getVehicle(vehicleId, VehicleState.IN_REPAIR);
        vehicleService.finishRepairsAndChargeCompany(vehicle, employee, totalCost, bankAccountNumber, dueDate);
    }

    @Transactional
    public void finishRepairsAndChargeLastCustomerThatRentedCar(
            final @NonNull UUID employeeId, final @NonNull UUID vehicleId,
            final @NonNull BigDecimal totalCost, final @NonNull String bankAccountNumber,
            final @NonNull LocalDate dueDate) {
        userContextValidator.checkUserPerformingAction(employeeId);
        final EmployeeEntity employee = getEmployee(employeeId);
        final VehicleEntity vehicle = vehicleService.getVehicle(vehicleId, VehicleState.IN_REPAIR);
        vehicleService.finishRepairsAndChargeCustomer(vehicle, employee, totalCost, bankAccountNumber, dueDate);
    }

    @Override
    @Transactional
    public void returnVehicleAndMakeReadyToRent(@NonNull final UUID rentalId,
                                                @NonNull final UUID employeeId) {
        userContextValidator.checkUserPerformingAction(employeeId);
        final VehicleEntity vehicle = finishRental(rentalId, employeeId);
        vehicleService.makeVehicleReadyToRent(vehicle);
    }

    @Override
    @Transactional
    public void returnVehicleAndSentItToRepairs(@NonNull final UUID rentalId,
                                                @NonNull final UUID employeeId) {
        userContextValidator.checkUserPerformingAction(employeeId);
        final VehicleEntity vehicle = finishRental(rentalId, employeeId);
        vehicleService.sendToRepair(vehicle);
    }

    @Override
    @Transactional
    public void insureVehicleAndMakeInsured(final @NonNull UUID employeeId,
                                            final @NonNull UUID vehicleId,
                                            final @NonNull String insuranceId,
                                            final @NonNull String bankAccountNumber,
                                            final @NonNull BigDecimal insuranceCost,
                                            final @NonNull LocalDate dueDate) {
        userContextValidator.checkUserPerformingAction(employeeId);
        final EmployeeEntity employee = getEmployee(employeeId);
        final VehicleEntity vehicle = vehicleService.getVehicle(vehicleId, Set.of(VehicleState.NEW, VehicleState.NOT_INSURED));
        vehicleService.makeVehicleInsured(vehicle, employee, insuranceId, bankAccountNumber, insuranceCost, dueDate);
    }

    @Override
    @Transactional
    public void sendInsuredOrJustReturnedVehicleToRepairs(final @NonNull UUID employeeId,
                                                          final @NonNull UUID vehicleId) {
        userContextValidator.checkUserPerformingAction(employeeId);
        final VehicleEntity vehicle = vehicleService.getVehicle(vehicleId, Set.of(VehicleState.INSURED, VehicleState.JUST_RETURNED));
        vehicleService.sendToRepair(vehicle);
    }

    @Transactional
    public void makeInsuredVehicleReadyToRent(final @NonNull UUID employeeId,
                                              final @NonNull UUID vehicleId) {
        userContextValidator.checkUserPerformingAction(employeeId);
        final VehicleEntity vehicle = vehicleService.getVehicle(vehicleId, VehicleState.INSURED);
        vehicleService.makeVehicleReadyToRent(vehicle);
    }

    @NonNull
    private EmployeeEntity getEmployee(final @NonNull UUID employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found"));
    }

    private VehicleEntity finishRental(final @NonNull UUID rentalId, final @NonNull UUID employeeId) {
        final EmployeeEntity employee = getEmployee(employeeId);
        final RentalEntity activeRental = rentalService.getRental(rentalId);

        final VehicleEntity vehicle = vehicleService.returnVehicle(activeRental);
        final RentalEntity rental = rentalService.finishRental(rentalId, employee);

        paymentService.createOverRentPaymentIfNecessary(rental, employee);
        return vehicle;
    }

    private EmployeeModel toEmployeeModel(final @NonNull EmployeeEntity employee) {
        return EmployeeModel.builder()
                .userId(employee.getUserId())
                .employeeId(employee.getEmployeeIdentifier())
                .email(employee.getEmail())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .phoneNumber(employee.getPhoneNumber())
                .nationalId(employee.getNationalId())
                .isActive(employee.isActive())
                .firstLoginDone(employee.getFirstLoginDone())
                .isAdmin(employee instanceof AdminEntity)
                .build();
    }
}
