package psk.bio.car.rental.infrastructure.spring.delivery.http;

import jakarta.validation.Valid;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import psk.bio.car.rental.api.security.LoginRequest;


@RestController
public class AuthenticationHttpEndpoint {

    @SuppressWarnings("unused")
    @PostMapping(value = "/login", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public void login(final @Valid @RequestBody LoginRequest loginRequest) {
        throw new NotImplementedException("/login should not be called");
    }

}
