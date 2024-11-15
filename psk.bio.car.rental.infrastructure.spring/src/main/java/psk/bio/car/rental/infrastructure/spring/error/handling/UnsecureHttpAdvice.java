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
import psk.bio.car.rental.api.errors.ErrorResponse;
import psk.bio.car.rental.application.profiles.ApplicationProfile;
import psk.bio.car.rental.application.security.exceptions.BusinessException;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component("customHttpAdvice")
@Profile(ApplicationProfile.UNSECURE_ERRORS)
@ControllerAdvice(basePackages = "psk.bio.car")
public class UnsecureHttpAdvice implements AuthenticationEntryPoint {

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse res, final AuthenticationException authException)
            throws IOException {
        var response = mapToErrorResponse(HttpStatus.UNAUTHORIZED, authException, request.getRequestURI());
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        OutputStream responseStream = res.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(responseStream, response);
        responseStream.flush();
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleException(final AuthenticationException authException, final HttpServletRequest request) {
        var response = mapToErrorResponse(HttpStatus.UNAUTHORIZED, authException, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleException(final BusinessException ex, final HttpServletRequest request) {
        var response = mapToErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleException(final ResponseStatusException ex, final HttpServletRequest request) {
        HttpStatus httpStatus = HttpStatus.resolve(ex.getStatusCode().value());
        if (httpStatus == null) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        var response = mapToErrorResponse(httpStatus, ex, request.getRequestURI());
        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception ex, final HttpServletRequest request) {
        var response = mapToErrorResponse(ex, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private ErrorResponse mapToErrorResponse(final Exception exception, final String path) {
        return ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .uriPath(path)
                .exception(exception.getClass().getSimpleName())
                .stackTrace(Arrays.toString(exception.getStackTrace()))
                .build();
    }

    private ErrorResponse mapToErrorResponse(final @NonNull HttpStatus status, final @NonNull Exception exception,
                                             final @NonNull String path) {
        return ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(LocalDateTime.now())
                .uriPath(path)
                .exception(exception.getClass().getSimpleName())
                .stackTrace(Arrays.toString(exception.getStackTrace()))
                .businessError(status.equals(HttpStatus.UNPROCESSABLE_ENTITY) ? exception.getMessage() : null)
                .build();
    }

}
