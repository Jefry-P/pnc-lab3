package com.ncapas.lab3.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

public class DotenvInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();

            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            Map<String, Object> dotenvProperties = new HashMap<>();

            dotenv.entries().forEach(entry -> {
                // Only load if not already set in System environment/properties
                if (environment.getProperty(entry.getKey()) == null) {
                    dotenvProperties.put(entry.getKey(), entry.getValue());
                }
            });

            if (!dotenvProperties.isEmpty()) {
                environment.getPropertySources().addLast(new MapPropertySource("dotenvProperties", dotenvProperties));
            }
        } catch (Exception e) {
            // Ignore errors loading .env
        }
    }
}
