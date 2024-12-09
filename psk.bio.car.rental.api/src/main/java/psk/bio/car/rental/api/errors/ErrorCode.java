package psk.bio.car.rental.api.errors;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INTERNAL_ERROR("general-server-error", 500),
    INVALID_REQUEST("invalid-request-error", 400),
    UNAUTHORIZED("unauthorized-error", 401),
    PERMISSION_DENIED("permission-denied-error", 403),
    NOT_FOUND("not-found-error", 404),
    EXPIRED_AUTH("expired-auth-token-error", 499),
    BUSINESS_ERROR("business-error", 422);

    @NonNull
    private final String code;
    private final int status;

    public static ErrorCode resolveCode(final int status) {
        return Arrays.stream(ErrorCode.values())
                .filter(errorCode -> errorCode.getStatus() == status)
                .findFirst()
                .orElse(INTERNAL_ERROR);
    }
}
