package com.oudom.minideploymanager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow all endpoints
                .allowedOrigins("*") // Allow any origin (good for dev)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
