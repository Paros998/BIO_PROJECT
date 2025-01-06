package psk.bio.car.rental.application.security.exceptions;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BusinessExceptionCodes {
    public static final String USER_WITH_SAME_USERNAME_ALREADY_EXISTS = "USER_WITH_SAME_USERNAME_ALREADY_EXISTS";
    public static final String USER_PERFORMING_ACTION_NOT_ON_THEMSELVES = "USER_PERFORMING_ACTION_NOT_ON_THEMSELVES";
    public static final String USER_PERFORMING_ACTION_NOT_EXISTS = "USER_PERFORMING_ACTION_NOT_EXISTS";
}
