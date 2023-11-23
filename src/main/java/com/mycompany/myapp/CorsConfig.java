package com.mycompany.myapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // Configura los mismos valores que en addCorsMappings
        config.applyPermitDefaultValues();

        // Agrega el método DELETE a los métodos permitidos
        config.addAllowedMethod(HttpMethod.DELETE);

        // Agrega otros métodos permitidos según sea necesario (PUT, POST, etc.)
        config.addAllowedMethod(HttpMethod.PUT);
        config.addAllowedMethod(HttpMethod.POST);

        // Agrega encabezados personalizados que deseas permitir
        config.addAllowedHeader("Authorization");
        config.addAllowedHeader("Content-Disposition");

        // Exponer los siguientes encabezados a la respuesta (si es necesario)
        config.addExposedHeader("Authorization");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
