package psk.bio.car.rental.infrastructure.spring.error.handling;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CustomFilterAdvice {
    void commence(HttpServletRequest request, HttpServletResponse res, Exception exception) throws IOException;
}
