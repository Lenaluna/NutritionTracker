package com.example.NutritionTracker.config;

import com.example.NutritionTracker.dto.FoodItemDTO;
import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.entity.User;
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
import java.util.UUID;

@Component
@Profile({"dev", "test"})
@RequiredArgsConstructor
public class DevDataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DevDataLoader.class);

    private final FoodItemService foodItemService;
    private final NutritionLogService nutritionLogService;
    private final UserService userService;

    @Override
    public void run(String... args) {
        logger.info("Loading test data...");

        nutritionLogService.cleanup();
        foodItemService.cleanup();
        userService.cleanup();

        logger.info("Test data cleaned up.");

        User user = User.builder()
                .name("Test User")
                .age(30)
                .weight(70.0)
                .isAthlete(false)
                .build();
        userService.createUser(user);
        logger.info("Test User created.");

        List<FoodItemDTO> foodItemsToSave = List.of(
                new FoodItemDTO(UUID.randomUUID(), "Chicken Breast", Map.of("Lysine", 2.5, "Valine", 3.0)),
                new FoodItemDTO(UUID.randomUUID(), "Quinoa", Map.of("Lysine", 1.2, "Valine", 2.0)),
                new FoodItemDTO(UUID.randomUUID(), "Lentils", Map.of("Lysine", 2.1, "Valine", 2.8))
        );

        List<FoodItemDTO> savedFoodItems = foodItemService.saveAllFoodItems(foodItemsToSave);

        logger.info("Test food items saved to database.");

        NutritionLog log = NutritionLog.builder()
                .logDateTime(LocalDateTime.now())
                .user(user)
                .build();

        NutritionLog savedLog = nutritionLogService.createLog(log);
        logger.info("Test nutrition log created in database.");

        for (FoodItemDTO item : savedFoodItems) {
            nutritionLogService.addFoodItemToLog(savedLog.getId(), item.getId());
        }

        logger.info("Test nutrition log linked with food items.");
    }
}