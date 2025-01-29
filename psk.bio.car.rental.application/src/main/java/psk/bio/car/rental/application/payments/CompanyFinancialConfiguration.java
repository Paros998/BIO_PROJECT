package psk.bio.car.rental.application.payments;

import lombok.NonNull;

public interface CompanyFinancialConfiguration {
    @NonNull
    String getCompanyBankAccountNumber();
}
