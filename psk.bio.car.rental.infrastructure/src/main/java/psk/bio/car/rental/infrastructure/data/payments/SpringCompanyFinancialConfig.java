package psk.bio.car.rental.infrastructure.data.payments;

import lombok.*;
import psk.bio.car.rental.application.payments.CompanyFinancialConfiguration;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpringCompanyFinancialConfig implements CompanyFinancialConfiguration {
    private String accountNumber;

    @Override
    public @NonNull String getCompanyBankAccountNumber() {
        return String.copyValueOf(accountNumber.toCharArray());
    }
}
