package com.htecgroup.flightadvisor.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@Data
public class ApplicationProperties {

    private Security security = new Security();
    private CorsConfiguration cors = new CorsConfiguration();
    private String uploadDirectory = "data";

    @Data
    public static class Security {
        private String base64Secret;
        private Long tokenValidityInSeconds;
    }
}
