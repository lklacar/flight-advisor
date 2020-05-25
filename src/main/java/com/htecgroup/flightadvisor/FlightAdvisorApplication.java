package com.htecgroup.flightadvisor;

import com.htecgroup.flightadvisor.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@SpringBootApplication
@EnableConfigurationProperties({LiquibaseProperties.class, ApplicationProperties.class})
public class FlightAdvisorApplication {

    public static void main(String[] args) {
        SpringApplication.run(FlightAdvisorApplication.class);
    }
}
