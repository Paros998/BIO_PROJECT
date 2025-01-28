package psk.bio.car.rental.application.security.exceptions;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.Map;

import static psk.bio.car.rental.application.security.exceptions.BusinessExceptionCodes.*;

@UtilityClass
public class BusinessExceptionFactory {
    private static final String DEFAULT_MESSAGE = "App business error occurred, try again later.";

    private static final Map<String, String> BUSINESS_CODE_TO_MESSAGE = Map.of(
            USER_WITH_SAME_USERNAME_ALREADY_EXISTS, "User with same username already exists",
            USER_PERFORMING_ACTION_NOT_ON_THEMSELVES, "User performing action not on the themselves",
            USER_PERFORMING_ACTION_NOT_EXISTS, "User performing action not exists",
            VEHICLE_WITH_SAME_PLATE_ALREADY_EXISTS, "Vehicle with same plate already exists",
            VEHICLE_IS_NOT_RENTED, "Vehicle is not rented",
            VEHICLE_IS_ALREADY_RENTED, "Vehicle is already rented",
            VEHICLE_INSURANCE_DUE_DATE_IS_INCORRECT, "Vehicle insurance due date is incorrect"
    );

    public static BusinessException instantiateBusinessException(final @NonNull String code) {
        return new BusinessException(BUSINESS_CODE_TO_MESSAGE.getOrDefault(code, DEFAULT_MESSAGE));
    }

    public static BusinessException instantiateBusinessException(final @NonNull String code, @NonNull final Exception ex) {
        return new BusinessException(BUSINESS_CODE_TO_MESSAGE.getOrDefault(code, DEFAULT_MESSAGE), ex);
    }

}
