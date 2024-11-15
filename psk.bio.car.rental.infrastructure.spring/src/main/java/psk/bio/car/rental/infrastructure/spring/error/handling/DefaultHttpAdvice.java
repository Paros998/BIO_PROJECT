package psk.bio.car.rental.infrastructure.spring.error.handling;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import psk.bio.car.rental.api.errors.ErrorCode;
import psk.bio.car.rental.api.errors.SecureErrorResponse;
import psk.bio.car.rental.application.profiles.ApplicationProfile;
import psk.bio.car.rental.application.security.exceptions.BusinessException;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

@Component("customHttpAdvice")
@Profile(ApplicationProfile.SECURE_ERRORS)
@ControllerAdvice(basePackages = "psk.bio.car")
public class DefaultHttpAdvice implements AuthenticationEntryPoint {

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse res, final AuthenticationException authException)
            throws IOException {
        var response = mapToSecureErrorResponse(HttpStatus.UNAUTHORIZED, authException.getMessage());
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        OutputStream responseStream = res.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(responseStream, response);
        responseStream.flush();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<SecureErrorResponse> handleException(final AuthenticationException authException) {
        var response = mapToSecureErrorResponse(HttpStatus.UNAUTHORIZED, null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<SecureErrorResponse> handleException(final BusinessException ex, final HttpServletRequest request) {
        var response = mapToSecureErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<SecureErrorResponse> handleException(final ResponseStatusException ex, final HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.resolve(ex.getStatusCode().value());
        if (httpStatus == null) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        var response = mapToSecureErrorResponse(httpStatus, ex.getMessage());
        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SecureErrorResponse> handleException(final Exception ex, final HttpServletRequest request) {
        var response = mapToSecureErrorResponse();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private SecureErrorResponse mapToSecureErrorResponse() {
        return SecureErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .code(ErrorCode.INTERNAL_ERROR.getCode())
                .build();
    }

    private SecureErrorResponse mapToSecureErrorResponse(final @NonNull HttpStatus status, final String message) {
        return SecureErrorResponse.builder()
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .code(ErrorCode.resolveCode(status.value()).getCode())
                .businessError(status.equals(HttpStatus.UNPROCESSABLE_ENTITY) ? message : null)
                .build();
    }

}
