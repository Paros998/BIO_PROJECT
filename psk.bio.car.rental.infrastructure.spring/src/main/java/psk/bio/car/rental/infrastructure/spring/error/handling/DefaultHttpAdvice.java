package psk.bio.car.rental.infrastructure.spring.error.handling;

import static psk.bio.car.rental.infrastructure.spring.filters.jwt.JwtTokenRefresher.TOKEN_EXPIRED_STATUS;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

import lombok.extern.log4j.Log4j2;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import psk.bio.car.rental.api.errors.ErrorCode;
import psk.bio.car.rental.api.errors.SecureErrorResponse;
import psk.bio.car.rental.application.profiles.ApplicationProfile;
import psk.bio.car.rental.application.security.exceptions.BusinessException;

@Log4j2
@RequiredArgsConstructor
@Component("customHttpAdvice")
@Profile(ApplicationProfile.SECURE_ERRORS)
@ControllerAdvice(basePackages = "psk.bio.car")
public class DefaultHttpAdvice implements AuthenticationEntryPoint, CustomFilterAdvice  {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse res, final AuthenticationException authException)
            throws IOException {
        log.error(authException);
        var errorResponse = mapToSecureErrorResponse(HttpStatus.UNAUTHORIZED, authException.getMessage());
        writeResponse(res, errorResponse);
    }

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse res, final Exception exception) throws IOException {
        log.error(exception);
        var errorResponse = mapExceptionToJson(exception);
        writeResponse(res, errorResponse);
    }

    private void writeResponse(final HttpServletResponse response, final SecureErrorResponse errorResponse) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorResponse.getStatus());
        OutputStream responseStream = response.getOutputStream();
        objectMapper.writeValue(responseStream, errorResponse);
        responseStream.flush();
    }

    private SecureErrorResponse mapExceptionToJson(final @NonNull Exception exception) {
        if (exception instanceof ExpiredJwtException) {
            return mapToSecureErrorResponse(TOKEN_EXPIRED_STATUS, null);
        } else if (exception instanceof ResponseStatusException rse) {
            HttpStatus httpStatus = HttpStatus.resolve(rse.getStatusCode().value());
            if (httpStatus == null) {
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            return mapToSecureErrorResponse(httpStatus, null);
        } else {
            return mapToSecureErrorResponse();
        }
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<SecureErrorResponse> handleException(final AuthenticationException authException) {
        log.error(authException);
        var response = mapToSecureErrorResponse(HttpStatus.UNAUTHORIZED, authException.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<SecureErrorResponse> handleException(final BusinessException ex, final HttpServletRequest request) {
        log.error(ex);
        var response = mapToSecureErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<SecureErrorResponse> handleException(final ResponseStatusException ex, final HttpServletRequest request) {
        log.error(ex);
        HttpStatus httpStatus = HttpStatus.resolve(ex.getStatusCode().value());
        if (httpStatus == null) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        var response = mapToSecureErrorResponse(httpStatus, ex.getMessage());
        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SecureErrorResponse> handleException(final Exception ex, final HttpServletRequest request) {
        log.error(ex);
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

    private SecureErrorResponse mapToSecureErrorResponse(final Integer status, final String message) {
        return SecureErrorResponse.builder()
                .status(status)
                .timestamp(LocalDateTime.now())
                .code(ErrorCode.resolveCode(status).getCode())
                .businessError(status.equals(HttpStatus.UNPROCESSABLE_ENTITY.value()) ? message : null)
                .build();
    }
}
