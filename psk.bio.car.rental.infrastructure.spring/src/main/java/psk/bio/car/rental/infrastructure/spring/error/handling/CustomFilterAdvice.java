package psk.bio.car.rental.infrastructure.spring.error.handling;

public interface CustomFilterAdvice {
    String mapExceptionToJson(Exception exception, String path);
}
