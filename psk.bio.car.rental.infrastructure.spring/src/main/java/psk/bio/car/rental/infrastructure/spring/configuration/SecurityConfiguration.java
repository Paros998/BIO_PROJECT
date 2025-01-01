package psk.bio.car.rental.infrastructure.spring.configuration;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import psk.bio.car.rental.application.security.UserContextValidator;
import psk.bio.car.rental.application.security.UserRole;
import psk.bio.car.rental.application.user.UserRepository;
import psk.bio.car.rental.infrastructure.spring.filters.formlogin.FormLoginAuthenticationFilter;
import psk.bio.car.rental.infrastructure.spring.filters.jwt.JwtTokenFilter;
import psk.bio.car.rental.infrastructure.spring.security.SpringUserContextValidator;

import java.util.Arrays;
import java.util.List;


@Configuration
public class SecurityConfiguration {

    @Bean
    static @NonNull MethodSecurityExpressionHandler methodSecurityExpressionHandler(final @NonNull RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        final String acao = "Access-Control-Allow-Origin";

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "https://localhost:8080", "http://localhost:3000",
                "https://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(Arrays.asList("Origin", acao, "Content-Type", "Accept",
                "Authorization", "Origin , Accept", "X-Requested-With", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        configuration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept",
                "Authorization", acao, acao, "Access-Control-Allow-Credentials"));

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        String format = "ROLE_%s > ROLE_%s";

        List<String> hierarchies = List.of(
                String.format(format, UserRole.ADMIN.name(), UserRole.CLIENT.name()),
                String.format(format, UserRole.ADMIN.name(), UserRole.EMPLOYEE.name()),
                String.format(format, UserRole.EMPLOYEE.name(), UserRole.CLIENT.name())
        );

        var finalHierarchy = hierarchies.stream().reduce((s, s2) -> s.concat("\n" + s2)).orElse("");
        return RoleHierarchyImpl.fromHierarchy(finalHierarchy);
    }

    @Bean
    public UserContextValidator userContextValidator(final @NonNull UserRepository userRepository) {
        return new SpringUserContextValidator(userRepository);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final @NonNull HttpSecurity http,
                                                   final @NonNull AuthenticationManager authenticationManager,
                                                   final @NonNull FormLoginAuthenticationFilter formLoginAuthenticationFilter,
                                                   final @NonNull JwtTokenFilter jwtTokenFilter,
                                                   final @NonNull @Qualifier("customHttpAdvice")
                                                   AuthenticationEntryPoint authenticationEntryPoint) throws Exception {
        http
                .authenticationManager(authenticationManager)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractAuthenticationFilterConfigurer::disable)

                .logout(AbstractHttpConfigurer::disable)

                .addFilter(formLoginAuthenticationFilter)
                .addFilterAfter(jwtTokenFilter, FormLoginAuthenticationFilter.class)

                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(
                                "/",
                                "/actuator/**",
                                "/actuator/health/**",
                                "/swagger-ui/",
                                "/swagger-ui/**",
                                "/swagger-ui.html**",
                                "/v3/api-docs/**",
                                "/public/**",
                                "/favicon.ico",
                                "/error",
                                "/instances",
                                "/admin",
                                "/admin/**"
                        )
                        .permitAll()

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/users/register", "/login")
                        .anonymous()

                        .requestMatchers(
                                "/api/users/{userId}",
                                "/api/users/finish-register/{userId}"
                        )
                        .hasRole(UserRole.CLIENT.name())

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/vehicles/search**"
                        )
                        .hasRole(UserRole.CLIENT.name())

                        .requestMatchers(
                                "/api/vehicles/**",
                                "/api/employees/finish-first-login"
                        ).hasRole(UserRole.EMPLOYEE.name())

                        .requestMatchers(
                                "/api/users",
                                "/api/admins/**",
                                "/api/employees/**"
                        )
                        .hasRole(UserRole.ADMIN.name())
                )

                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint(authenticationEntryPoint)
                );

        return http.build();
    }
}
