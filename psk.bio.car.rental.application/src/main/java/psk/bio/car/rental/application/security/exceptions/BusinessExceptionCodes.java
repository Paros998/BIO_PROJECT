package psk.bio.car.rental.application.security.exceptions;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BusinessExceptionCodes {
    public static final String USER_WITH_SAME_USERNAME_ALREADY_EXISTS = "USER_WITH_SAME_USERNAME_ALREADY_EXISTS";
    public static final String USER_PERFORMING_ACTION_NOT_ON_THEMSELVES = "USER_PERFORMING_ACTION_NOT_ON_THEMSELVES";
    public static final String USER_PERFORMING_ACTION_NOT_EXISTS = "USER_PERFORMING_ACTION_NOT_EXISTS";
    public static final String VEHICLE_WITH_SAME_PLATE_ALREADY_EXISTS = "VEHICLE_WITH_SAME_PLATE_ALREADY_EXISTS";
    public static final String VEHICLE_IS_NOT_RENTED = "VEHICLE_IS_NOT_RENTED";
    public static final String VEHICLE_IS_ALREADY_RENTED = "VEHICLE_IS_ALREADY_RENTED";
    public static final String VEHICLE_INSURANCE_DUE_DATE_IS_INCORRECT = "VEHICLE_INSURANCE_DUE_DATE_IS_INCORRECT";
}
