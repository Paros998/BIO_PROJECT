package psk.bio.car.rental.infrastructure.spring.filters.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.assertj.core.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;
import psk.bio.car.rental.application.user.UserProjection;
import psk.bio.car.rental.application.user.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static psk.bio.car.rental.infrastructure.spring.filters.jwt.JwtExpire.ACCESS_TOKEN;

@RequiredArgsConstructor
public class JwtTokenRefresher {
    private static final String AUTH_BEARER = "Bearer";
    private static final String AUTH_HEADER = "Authorization-Refresh";
    private static final int TOKEN_REFRESHED_STATUS = 299;
    private static final int TOKEN_EXPIRED_STATUS = 499;

    private final UserRepository userRepository;
    private final String secretKey;

    public void attemptRefreshToken(final HttpServletRequest request, final HttpServletResponse response) {
        final String authorizationHeader = request.getHeader(AUTH_HEADER);

        if (!Strings.isNullOrEmpty(authorizationHeader) && authorizationHeader.startsWith(AUTH_BEARER + " ")) {
            final String token = authorizationHeader.replace(AUTH_BEARER + " ", "");
            try {
                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                        .build()
                        .parseClaimsJws(token);

                Claims body = claimsJws.getBody();

                String userId = body.get("userId", String.class);

                UserProjection user = userRepository.findById(userId).orElseThrow();

                String accessToken = Jwts.builder()
                        .setSubject(user.getUsername())
                        .claim("authorities", user.getAuthorities())
                        .claim("userId", userId)
                        .setIssuedAt(new Date())
                        .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN.getAmount()))
                        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                        .compact();

                response.addHeader("Authorization", AUTH_BEARER + " " + accessToken);
                response.setStatus(TOKEN_REFRESHED_STATUS);

            } catch (final Exception e) {
                if (e.getClass().equals(ExpiredJwtException.class)) {
                    throw new ResponseStatusException(HttpStatusCode.valueOf(TOKEN_EXPIRED_STATUS),
                            String.format("Token %s has expired, please login again", token));
                } else if (e.getClass().equals(SignatureException.class)) {
                    throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
                } else {
                    throw e;
                }
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, AUTH_HEADER + " token hasn't been provided with request");
        }
    }
}
