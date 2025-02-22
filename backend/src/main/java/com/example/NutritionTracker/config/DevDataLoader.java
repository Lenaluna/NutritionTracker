package com.example.NutritionTracker.config;

import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.exception.UserAlreadyExistsException;
import com.example.NutritionTracker.service.FoodItemService;
import com.example.NutritionTracker.service.NutritionLogService;
import com.example.NutritionTracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DevDataLoader is responsible for populating the database with test data when
 * the application is running in the development profile. It creates sample food
 * items and a test nutrition log to simulate user interactions.
 */
@Component
@Profile({"dev","test"})// Runs only when the "dev" profile is active
@RequiredArgsConstructor
public class DevDataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DevDataLoader.class);

    private final FoodItemService foodItemService;
    private final NutritionLogService nutritionLogService;
    private final UserService userService;

    @Override
    public void run(String... args) {
        logger.info("Loading test data...");

        userService.cleanup();
        User user = User.builder()
                .name("Test User")
                .age(30)
                .weight(70.0)
                .isAthlete(false)
                .build();
        userService.createUser(user);
        logger.info("Test User created.");

        // Create sample FoodItems
        FoodItem chickenBreast = FoodItem.builder()
                .name("Chicken Breast")
                .aminoAcidProfile(Map.of("Lysine", 2.5, "Valine", 3.0))
                .build();

        FoodItem quinoa = FoodItem.builder()
                .name("Quinoa")
                .aminoAcidProfile(Map.of("Lysine", 1.2, "Valine", 2.0))
                .build();

        FoodItem lentils = FoodItem.builder()
                .name("Lentils")
                .aminoAcidProfile(Map.of("Lysine", 2.1, "Valine", 2.8))
                .build();

        // Save FoodItems to H2 database
        foodItemService.saveAllFoodItems(List.of(chickenBreast, quinoa, lentils));
        logger.info("Test food items saved to database.");

        // Create a sample NutritionLog
        NutritionLog log = NutritionLog.builder()
                .foodItems(List.of(chickenBreast, quinoa)) // Link existing food items to the log
                .logDateTime(LocalDateTime.now()) // Set the new date-time field
                .user(user) // Assign user to the log
                .build();

        // Save NutritionLog to H2 database
        nutritionLogService.createLog(log);
        logger.info("Test nutrition log created in database.");
    }
}