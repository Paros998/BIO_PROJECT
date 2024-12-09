package psk.bio.car.rental.infrastructure.spring.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import psk.bio.car.rental.application.profiles.ApplicationProfile;

import java.util.List;

@Profile(ApplicationProfile.SWAGGER)
@Configuration
@RestController
public class SwaggerConfiguration {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("all")
                .pathsToMatch("/**")
                .pathsToExclude("/")
                .build();
    }

    @Bean
    public GroupedOpenApi apiV1() {
        return GroupedOpenApi.builder()
                .group("v1")
                .pathsToMatch("/api/v1/**")
                .pathsToExclude("/", "/swagger-ui/", "/swagger-resources/**", "/actuator/**")
                .build();
    }

    @Bean
    public OpenAPI openAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info().title("API v1")
                        .description("Simple API")
                        .version("v0.2")
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .security(List.of(new SecurityRequirement().addList(securitySchemeName)))
                .externalDocs(new ExternalDocumentation()
                        .description("Simple API Wiki Documentation")
                        .url("https://github.com/Paros998/BIO_PROJECT"));
    }

    @GetMapping(value = {"/swagger-ui", "/swagger-ui/"})
    public RedirectView redirectView() {
        return new RedirectView("/swagger-ui.html");
    }
}
