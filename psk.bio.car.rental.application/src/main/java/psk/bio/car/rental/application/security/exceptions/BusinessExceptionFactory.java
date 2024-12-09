package psk.bio.car.rental.application.security.exceptions;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.Map;

@UtilityClass
public class BusinessExceptionFactory {
    private static final String DEFAULT_MESSAGE = "App business error occurred, try again later.";

    private static final Map<String, String> BUSINESS_CODE_TO_MESSAGE = Map.of(

    );

    public static BusinessException instantiateBusinessException(final @NonNull String code) {
        return new BusinessException(BUSINESS_CODE_TO_MESSAGE.getOrDefault(code, DEFAULT_MESSAGE));
    }

    public static BusinessException instantiateBusinessException(final @NonNull String code, @NonNull final Exception ex) {
        return new BusinessException(BUSINESS_CODE_TO_MESSAGE.getOrDefault(code, DEFAULT_MESSAGE), ex);
    }

}
