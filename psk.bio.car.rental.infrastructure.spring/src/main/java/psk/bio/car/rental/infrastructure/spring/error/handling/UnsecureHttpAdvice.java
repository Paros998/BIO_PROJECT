package psk.bio.car.rental.infrastructure.spring.error.handling;

import static psk.bio.car.rental.infrastructure.spring.filters.jwt.JwtTokenRefresher.TOKEN_EXPIRED_STATUS;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Arrays;

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
import psk.bio.car.rental.api.errors.ErrorResponse;
import psk.bio.car.rental.application.profiles.ApplicationProfile;
import psk.bio.car.rental.application.security.exceptions.BusinessException;

@RequiredArgsConstructor
@Component("customHttpAdvice")
@Profile(ApplicationProfile.UNSECURE_ERRORS)
@ControllerAdvice(basePackages = "psk.bio.car")
public class UnsecureHttpAdvice implements AuthenticationEntryPoint, CustomFilterAdvice {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse res, final AuthenticationException authException)
            throws IOException {
        var errorResponse = mapToErrorResponse(HttpStatus.UNAUTHORIZED, authException, request.getRequestURI());
        writeResponse(res, errorResponse);
    }

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse res, final Exception exception) throws IOException {
        var errorResponse = mapExceptionToJson(exception, request.getRequestURI());
        writeResponse(res, errorResponse);
    }

    private void writeResponse(final HttpServletResponse response, final ErrorResponse errorResponse) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(errorResponse.getStatus());
        OutputStream responseStream = response.getOutputStream();
        objectMapper.writeValue(responseStream, errorResponse);
        responseStream.flush();
    }

    private ErrorResponse mapExceptionToJson(final @NonNull Exception exception, final String path) {
        if (exception instanceof ExpiredJwtException e){
            return mapToErrorResponse(TOKEN_EXPIRED_STATUS, e, path);
        }
        else if (exception instanceof ResponseStatusException rse) {
            HttpStatus httpStatus = HttpStatus.resolve(rse.getStatusCode().value());
            if (httpStatus == null) {
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            }
            return mapToErrorResponse(httpStatus, exception, path);
        } else {
            return mapToErrorResponse(exception, path);
        }
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
                                             final String path) {
        return ErrorResponse.builder()
                .status(status.value())
                .timestamp(LocalDateTime.now())
                .uriPath(path)
                .exception(exception.getClass().getSimpleName())
                .stackTrace(Arrays.toString(exception.getStackTrace()))
                .businessError(status.equals(HttpStatus.UNPROCESSABLE_ENTITY) ? exception.getMessage() : null)
                .build();
    }

    private ErrorResponse mapToErrorResponse(final @NonNull Integer status, final @NonNull Exception exception,
                                             final String path) {
        return ErrorResponse.builder()
                .status(status)
                .timestamp(LocalDateTime.now())
                .uriPath(path)
                .exception(exception.getClass().getSimpleName())
                .stackTrace(Arrays.toString(exception.getStackTrace()))
                .businessError(status.equals(HttpStatus.UNPROCESSABLE_ENTITY.value()) ? exception.getMessage() : null)
                .build();
    }
}
