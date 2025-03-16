package com.example.NutritionTracker.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * This class is executed at application startup.
 * It logs a message indicating that the application has successfully started.
 *
 * Implementing {@link CommandLineRunner} ensures that the {@code run()} method
 * is executed after the application context has been initialized.
 */
@Component
public class StartupRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(StartupRunner.class);

    /**
     * Executes upon application startup.
     *
     * @param args Command-line arguments passed to the application (not used here).
     */
    @Override
    public void run(String... args) {
        logger.info("Application startup completed.");
    }
}