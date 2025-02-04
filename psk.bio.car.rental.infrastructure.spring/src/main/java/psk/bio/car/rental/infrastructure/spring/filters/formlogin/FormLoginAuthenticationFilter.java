package psk.bio.car.rental.infrastructure.spring.filters.formlogin;

import static psk.bio.car.rental.infrastructure.spring.filters.jwt.JwtExpire.ACCESS_TOKEN;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import psk.bio.car.rental.application.user.UserProjection;
import psk.bio.car.rental.application.user.UserRepository;
import psk.bio.car.rental.infrastructure.spring.error.handling.CustomFilterAdvice;

@RequiredArgsConstructor
public class FormLoginAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final boolean POST_ONLY = true;
    private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
            .getContextHolderStrategy();

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final AuthenticationEntryPointFailureHandler failureHandler;
    private final CustomFilterAdvice customFilterAdvice;
    private final String secretKey;

    @Value("${jwt.expire:9999999}")
    private Integer jwtExpirationInMillis;

    @Override
    public Authentication attemptAuthentication(final @NonNull HttpServletRequest request, final @NonNull HttpServletResponse response)
            throws AuthenticationException {
        if (POST_ONLY && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String username = this.obtainUsername(request);
        username = username != null ? username : "";
        username = username.trim();

        String password = this.obtainPassword(request);
        password = password != null ? password : "";

        final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);

        this.setDetails(request, authRequest);

        return authenticationManager.authenticate(authRequest);

    }

    @Override
    protected void unsuccessfulAuthentication(final HttpServletRequest request, final HttpServletResponse response,
                                              final AuthenticationException failed)
            throws IOException, ServletException {
        this.securityContextHolderStrategy.clearContext();
        this.logger.trace("Failed to process authentication request", failed);
        this.logger.trace("Cleared SecurityContextHolder");
        this.logger.trace("Handling authentication failure");
        this.getRememberMeServices().loginFail(request, response);
        this.failureHandler.onAuthenticationFailure(request, response, failed);
    }

    @Override
    protected void successfulAuthentication(final @NonNull HttpServletRequest request, final @NonNull HttpServletResponse response,
                                            final @NonNull FilterChain chain, final @NonNull Authentication authResult)
            throws IOException, ServletException {
        try {
            final String username = this.obtainUsername(request);

            final UUID userId = userRepository.findByUsername(username)
                    .map(UserProjection::getUserId)
                    .orElseThrow();

            final Integer jwtExpire = jwtExpirationInMillis == 0 ? ACCESS_TOKEN.getAmount() : jwtExpirationInMillis;

            String accessToken = Jwts.builder()
                    .setSubject(authResult.getName())
                    .claim("authorities", authResult.getAuthorities())
                    .claim("userId", userId)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpire))
                    .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                    .compact();

//            String refreshToken = Jwts.builder()
//                    .setSubject(authResult.getName())
//                    .claim("userId", userId)
//                    .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN.getAmount()))
//                    .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
//                    .compact();


            response.addHeader("Authorization", "Bearer " + accessToken);

//            response.addHeader("Authorization-Refresh", "Bearer " + refreshToken);

        } catch (final Exception e) {
            customFilterAdvice.commence(request, response, e);
        }
    }
}
