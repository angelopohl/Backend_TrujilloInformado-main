package com.segat.trujilloinformado.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**")
            // DEV: tu Vite corre en 8081
            .allowedOrigins("http://localhost:8081")
            // Si publicas luego, agrega aquí tu dominio (ejemplo):
            // .allowedOrigins("http://localhost:8081", "https://tu-dominio.com")
            .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(false)
            .maxAge(3600);
      }
    };
  }
}
