package psk.bio.car.rental.application.security.exceptions;

public class BusinessException extends RuntimeException {
    public BusinessException(final String businessMessage) {
        super(businessMessage);
    }

    public BusinessException(final String businessMessage, final Throwable cause) {
        super(businessMessage, cause);
    }
}
