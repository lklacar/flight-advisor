package com.htecgroup.flightadvisor.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.ServletContext;

@Configuration
@Slf4j
public class WebConfigurer implements ServletContextInitializer {

    private final Environment env;

    private final ApplicationProperties applicationProperties;

    public WebConfigurer(Environment env, ApplicationProperties applicationProperties) {
        this.env = env;
        this.applicationProperties = applicationProperties;
    }

    @Override
    public void onStartup(ServletContext servletContext) {
        if (env.getActiveProfiles().length != 0) {
            log.info("Web application configuration, using profiles: {}", env.getActiveProfiles());
        }
        log.info("Web application fully configured");
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = applicationProperties.getCors();
        if (config.getAllowedOrigins() != null && !config.getAllowedOrigins()
                .isEmpty()) {
            log.debug("Registering CORS filter");
            source.registerCorsConfiguration("/api/**", config);
            source.registerCorsConfiguration("/v2/api-docs", config);
        }
        return new CorsFilter(source);
    }
}
