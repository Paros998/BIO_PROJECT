package psk.bio.car.rental.infrastructure.spring.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import psk.bio.car.rental.application.payments.CompanyFinancialConfiguration;
import psk.bio.car.rental.infrastructure.data.payments.SpringCompanyFinancialConfig;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class CommonConfiguration {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        final JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(javaTimeModule);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

    @Bean
    @ConfigurationProperties(prefix = "business.configuration.company")
    public CompanyFinancialConfiguration companyFinancialConfiguration() {
        return new SpringCompanyFinancialConfig();
    }
}
