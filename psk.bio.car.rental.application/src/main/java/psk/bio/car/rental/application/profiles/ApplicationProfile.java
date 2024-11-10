package psk.bio.car.rental.application.profiles;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApplicationProfile {
    public static final String DEVLOCAL = "DEVLOCAL";

    public static final String SWAGGER = "SWAGGER";

    public static final String SECURE_ERRORS = "SECURE_ERRORS";
    public static final String INSECURE_ERRORS = "INSECURE_ERRORS";

    public static final String SPRING_BOOT_ACTIVE = "SBA";

    public static final String MD5_ENCRYPTION = "MD5_ENCRYPTION";
    public static final String BCRYPT_ENCRYPTION = "BCRYPT_ENCRYPTION";
}
