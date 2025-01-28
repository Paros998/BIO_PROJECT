package psk.bio.car.rental.application.payments;

import java.math.BigDecimal;

public enum PaymentType {
    INSURANCE,
    RENTAL_FEE,
    RENTAL_OVERDUE_FEE,
    REPAIR,
    CLIENT_REPAIR;

    public static final BigDecimal OVER_DUE_MODIFIER = new BigDecimal("1.5");
}
