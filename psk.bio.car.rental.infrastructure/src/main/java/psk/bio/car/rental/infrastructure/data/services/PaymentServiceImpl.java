package psk.bio.car.rental.infrastructure.data.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import psk.bio.car.rental.application.payments.CompanyFinancialConfiguration;
import psk.bio.car.rental.application.payments.PaymentStatus;
import psk.bio.car.rental.application.payments.PaymentType;
import psk.bio.car.rental.infrastructure.data.employee.EmployeeEntity;
import psk.bio.car.rental.infrastructure.data.payments.PaymentEntity;
import psk.bio.car.rental.infrastructure.data.payments.PaymentJpaRepository;
import psk.bio.car.rental.infrastructure.data.rentals.RentalEntity;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl {
    private static final Integer PAYMENT_DAYS = 7;

    private final PaymentJpaRepository paymentRepository;
    private final CompanyFinancialConfiguration financialConfiguration;

    @Transactional
    public void createOverRentPaymentIfNecessary(final @NonNull RentalEntity rental, final @NonNull EmployeeEntity employee) {
        final LocalDate currentDate = LocalDate.now();
        if (!rental.getRentEndDate().toLocalDate().isBefore(currentDate)) {
            return;
        }

        long daysOfOverDueRent = Duration.between(rental.getRentEndDate().toLocalDate(), currentDate).toDays();
        PaymentEntity clientChargedPayment = PaymentEntity.builder()
                .status(PaymentStatus.PENDING)
                .type(PaymentType.RENTAL_OVERDUE_FEE)
                .createdByEmployee(employee)
                .chargedClient(rental.getClient())
                .creationDate(LocalDateTime.now())
                .dueDate(LocalDate.now().plusDays(PAYMENT_DAYS))
                .associatedVehicle(rental.getVehicle())
                .associatedRental(rental)
                .accountNumber(financialConfiguration.getCompanyBankAccountNumber())
                .amount(rental.getVehicle().getRentPerDayPrice()
                        .multiply(PaymentType.OVER_DUE_MODIFIER)
                        .multiply(BigDecimal.valueOf(daysOfOverDueRent)))
                .build();
        paymentRepository.save(clientChargedPayment);
    }

    @Transactional
    public void save(final @NonNull PaymentEntity payment) {
        paymentRepository.save(payment);
    }
}
