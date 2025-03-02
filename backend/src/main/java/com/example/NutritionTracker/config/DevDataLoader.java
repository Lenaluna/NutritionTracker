package com.example.NutritionTracker.config;

import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.service.FoodItemService;
import com.example.NutritionTracker.service.NutritionLogService;
import com.example.NutritionTracker.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * DevDataLoader is responsible for populating the database with test data when
 * the application is running in the development or test profile.
 * It creates sample food items and a test nutrition log to simulate user interactions.
 */
@Component
@Profile({"dev"})
@RequiredArgsConstructor
public class DevDataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DevDataLoader.class);

    private final FoodItemService foodItemService;
    private final NutritionLogService nutritionLogService;
    private final UserService userService;

    @Override
    public void run(String... args) {
        logger.info("Loading test data...");


        // Cleanup existing test data
        userService.cleanup();

        // Create a test user
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

        // Save FoodItems and retrieve saved instances
        List<FoodItem> savedFoodItems = foodItemService.saveAllFoodItems(List.of(chickenBreast, quinoa, lentils));

        // Extract saved instances
        FoodItem savedChickenBreast = savedFoodItems.get(0);
        FoodItem savedQuinoa = savedFoodItems.get(1);
        FoodItem savedLentils = savedFoodItems.get(2);

        logger.info("Test food items saved to database.");

        // Create a sample NutritionLog
        NutritionLog log = NutritionLog.builder()
                .logDateTime(LocalDateTime.now()) // Set the new date-time field
                .user(user) // Assign user to the log
                .build();

        // Save NutritionLog to H2 database
        NutritionLog savedLog = nutritionLogService.createLog(log);
        logger.info("Test nutrition log created in database.");

        // Save NutritionLogFoodItems
        nutritionLogService.addFoodItemToLog(savedLog.getId(), savedChickenBreast.getId());
        nutritionLogService.addFoodItemToLog(savedLog.getId(), savedQuinoa.getId());
        nutritionLogService.addFoodItemToLog(savedLog.getId(), savedLentils.getId());

        logger.info("Test nutrition log linked with food items.");
    }
}