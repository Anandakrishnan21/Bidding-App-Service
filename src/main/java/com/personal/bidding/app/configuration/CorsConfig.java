package com.personal.bidding.app.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOrigins("http://localhost:3000",
                        "https://bidding-app-service-1.onrender.com/")
                .allowedMethods("POST", "GET", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
