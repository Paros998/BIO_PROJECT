package psk.bio.car.rental.application.security.utils;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RequiredArgsConstructor
public class Md5PasswordEncoder implements PasswordEncoder {
    private static final String ALGORITHM = "MD5";
    private final String salt;

    @Override
    public String encode(final CharSequence rawPassword) {
        return getSecurePassword(rawPassword.toString());
    }

    @Override
    public boolean matches(final CharSequence rawPassword, final String encodedPassword) {
        return encodedPassword.equals(getSecurePassword(rawPassword.toString()));
    }

    private String getSecurePassword(final @NonNull String passwordToHash) {
        String generatedPassword;
        try {
            final MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            final StringBuilder sb = new StringBuilder();

            md.update(salt.getBytes());
            byte[] bytes = md.digest(passwordToHash.getBytes());

            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16)
                        .substring(1));
            }
            generatedPassword = sb.toString();
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return generatedPassword;
    }
}