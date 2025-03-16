package com.example.NutritionTracker.config;

import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration class for Cross-Origin Resource Sharing (CORS).
 *
 * This configuration allows the frontend (running on localhost:5173)
 * to communicate with the backend by enabling cross-origin requests.
 */
@Configuration
public class WebConfig {

    /**
     * Creates and configures a CORS filter.
     *
     * This filter enables CORS requests from the specified frontend URL
     * (http://localhost:5173), allowing credentials and supporting
     * common HTTP methods.
     *
     * @return A {@link CorsFilter} that applies the defined CORS rules.
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow credentials (e.g., cookies, authentication headers)
        config.setAllowCredentials(true);

        // Allow requests from the specified frontend URL
        config.setAllowedOrigins(List.of("http://localhost:5173"));

        // Allow all headers
        config.setAllowedHeaders(List.of("*"));

        // Allow common HTTP methods
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Apply this configuration to all API endpoints
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}