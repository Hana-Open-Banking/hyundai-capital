package com.hyundai_capital.openbanking.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class TestConfig {
    
    /**
     * This configuration class is used to disable the DataInitializer during tests.
     * The DataInitializer is a CommandLineRunner that initializes test data in the database,
     * but it might cause issues during tests if it tries to access the database before it's fully set up,
     * or if it uses features that aren't compatible with H2.
     */
    
    @Configuration
    @ConditionalOnProperty(name = "spring.test.context", havingValue = "true", matchIfMissing = false)
    public static class DisableDataInitializerConfig {
        // This inner class will be loaded only during tests, and it will prevent the DataInitializer from being loaded
    }
}