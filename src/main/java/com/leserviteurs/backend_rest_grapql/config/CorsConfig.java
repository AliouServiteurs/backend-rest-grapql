package com.leserviteurs.backend_rest_grapql.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    /**
     * Configuration CORS pour autoriser les requêtes depuis le frontend React
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Autoriser les credentials (cookies, authorization headers)
        config.setAllowCredentials(true);

        // Origines autorisées (frontend React)
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000"      // Create React App
            // "http://localhost:5173"    // Vite
            // "http://localhost:4200"       // Angular (au cas où)
        ));

        // Headers autorisés
        config.setAllowedHeaders(Arrays.asList(
            "Origin",
            "Content-Type",
            "Accept",
            "Authorization",
            "X-Requested-With"
        ));

        // Méthodes HTTP autorisées
        config.setAllowedMethods(Arrays.asList(
            "GET",
            "POST",
            "PUT",
            "DELETE",
            "OPTIONS",
            "PATCH"
        ));

        // Appliquer la configuration à tous les endpoints
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}