package com.example.NutritionTracker.config;

import com.example.NutritionTracker.entity.AminoAcidRequirement;
import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.repo.AminoAcidRequirementRepository;
import com.example.NutritionTracker.repo.FoodItemRepository;
import com.example.NutritionTracker.repo.NutritionLogRepository;
import com.example.NutritionTracker.repo.UserRepository;
import com.example.NutritionTracker.service.FoodItemService;
import com.example.NutritionTracker.service.NutritionLogService;
import com.example.NutritionTracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This class is responsible for initializing test data in the development and test environments.
 * It ensures that predefined users, food items, and nutrition logs exist in the database.
 */
@Component
@Profile({"dev", "test"})
@RequiredArgsConstructor
public class DevDataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DevDataLoader.class);

    private final FoodItemRepository foodItemRepository;
    private final FoodItemService foodItemService;
    private final NutritionLogService nutritionLogService;
    private final UserService userService;
    private final AminoAcidRequirementRepository aminoAcidRequirementRepository;
    private final UserRepository userRepository;
    private final NutritionLogRepository nutritionLogRepository;


    /**
     * Runs the data initialization process. It performs cleanup and inserts test data if needed.
     *
     * @param args Command-line arguments (not used in this implementation).
     */
    @Override
    public void run(String... args) {
        logger.info("Starting DevDataLoader...");

        try {
            logger.info("Cleaning up old test data...");
            nutritionLogService.cleanup();
            foodItemService.cleanup();
            userService.cleanup();
            aminoAcidRequirementRepository.deleteAll();
            logger.info("Cleanup completed.");
        } catch (Exception e) {
            logger.error("Error during cleanup: ", e);
        }

        try {
            // Check if a user already exists
            Optional<User> existingUser = userRepository.findAll().stream().findFirst();           User user;
            if (existingUser.isPresent()) {
                user = existingUser.get();
                logger.warn("Ein Benutzer existiert bereits. Nutze vorhandenen Benutzer mit ID: {}", user.getId());
            } else {
                // Create a new test user
                user = User.builder()
                        .name("Test User")
                        .age(30)
                        .weight(70.0)
                        .isAthlete(false)
                        .isVegan(true)
                        .isLongevityFocused(true)
                        .build();
                userService.createUser(user);
                logger.info("Test User created with ID: {}", user.getId());
            }

            // Insert default food items if none exist
            if (foodItemRepository.count() == 0) {
                List<FoodItem> foodItemsToSave = List.of(
                        FoodItem.builder().name("Linsen").aminoAcidProfile(Map.of(
                                "Lysin", 2.1, "Leucin", 1.8, "Isoleucin", 0.9, "Valin", 1.0,
                                "Methionin", 0.3, "Phenylalanin", 1.2, "Threonin", 0.8, "Tryptophan", 0.2,
                                "Histidin", 0.7, "Glycin", 1.0)).build(),

                        FoodItem.builder().name("Quinoa").aminoAcidProfile(Map.of(
                                "Lysin", 1.2, "Leucin", 1.5, "Isoleucin", 0.7, "Valin", 0.9,
                                "Methionin", 0.4, "Phenylalanin", 1.0, "Threonin", 0.6, "Tryptophan", 0.1,
                                "Histidin", 0.5, "Glycin", 0.8)).build(),

                        FoodItem.builder().name("Kichererbsen").aminoAcidProfile(Map.of(
                                "Lysin", 2.0, "Leucin", 1.6, "Isoleucin", 0.8, "Valin", 1.1,
                                "Methionin", 0.3, "Phenylalanin", 1.3, "Threonin", 0.7, "Tryptophan", 0.2,
                                "Histidin", 0.6, "Glycin", 1.2)).build(),

                        FoodItem.builder().name("Sojabohnen").aminoAcidProfile(Map.of(
                                "Lysin", 2.8, "Leucin", 2.5, "Isoleucin", 1.5, "Valin", 1.7,
                                "Methionin", 0.6, "Phenylalanin", 1.8, "Threonin", 1.1, "Tryptophan", 0.3,
                                "Histidin", 1.0, "Glycin", 1.4)).build(),

                        FoodItem.builder().name("Amaranth").aminoAcidProfile(Map.of(
                                "Lysin", 1.3, "Leucin", 1.4, "Isoleucin", 0.6, "Valin", 0.7,
                                "Methionin", 0.4, "Phenylalanin", 0.9, "Threonin", 0.5, "Tryptophan", 0.1,
                                "Histidin", 0.4, "Glycin", 0.7)).build(),

                        FoodItem.builder().name("Hanfsamen").aminoAcidProfile(Map.of(
                                "Lysin", 1.9, "Leucin", 2.0, "Isoleucin", 1.2, "Valin", 1.4,
                                "Methionin", 0.8, "Phenylalanin", 1.6, "Threonin", 1.0, "Tryptophan", 0.3,
                                "Histidin", 0.9, "Glycin", 1.6)).build(),

                        FoodItem.builder().name("Chiasamen").aminoAcidProfile(Map.of(
                                "Lysin", 1.1, "Leucin", 1.3, "Isoleucin", 0.5, "Valin", 0.6,
                                "Methionin", 0.2, "Phenylalanin", 0.8, "Threonin", 0.4, "Tryptophan", 0.1,
                                "Histidin", 0.3, "Glycin", 0.9)).build(),

                        FoodItem.builder().name("Haferflocken").aminoAcidProfile(Map.of(
                                "Lysin", 0.8, "Leucin", 1.0, "Isoleucin", 0.5, "Valin", 0.6,
                                "Methionin", 0.3, "Phenylalanin", 0.7, "Threonin", 0.4, "Tryptophan", 0.1,
                                "Histidin", 0.2, "Glycin", 1.1)).build(),

                        FoodItem.builder().name("Sonnenblumenkerne").aminoAcidProfile(Map.of(
                                "Lysin", 1.5, "Leucin", 1.7, "Isoleucin", 0.9, "Valin", 1.1,
                                "Methionin", 0.7, "Phenylalanin", 1.5, "Threonin", 0.9, "Tryptophan", 0.3,
                                "Histidin", 0.8, "Glycin", 1.3)).build(),

                        FoodItem.builder().name("Erbsen").aminoAcidProfile(Map.of(
                                "Lysin", 2.2, "Leucin", 1.7, "Isoleucin", 0.8, "Valin", 1.0,
                                "Methionin", 0.3, "Phenylalanin", 1.2, "Threonin", 0.6, "Tryptophan", 0.2,
                                "Histidin", 0.7, "Glycin", 1.0)).build()
                );

                foodItemService.saveAllFoodItems(foodItemsToSave);
                logger.info("FoodItems gespeichert.");
            }

            // Ensure there is at least one NutritionLog for the user
            Optional<NutritionLog> existingLog = nutritionLogService.getLatestNutritionLogEntity();
            if (existingLog.isEmpty()) {
                NutritionLog log = NutritionLog.builder().user(user).build();
                nutritionLogService.createLog(log);
                NutritionLog savedLog = nutritionLogService.createLog(log);

                // Verify if the log was saved correctly
                if (savedLog.getId() == null) {
                    logger.error("FEHLER: NutritionLog wurde nicht korrekt gespeichert!");
                    throw new IllegalStateException("NutritionLog konnte nicht erstellt werden.");
                }

                // Double-check persistence
                savedLog = nutritionLogRepository.findById(savedLog.getId()).orElse(null);
                if (savedLog == null || savedLog.getId() == null) {
                    throw new IllegalStateException("Fehler beim Speichern von NutritionLog.");
                }

                logger.info("Test NutritionLog created.");
            }

            // Insert amino acid requirements if none exist
            if (aminoAcidRequirementRepository.count() == 0) {
                List<AminoAcidRequirement> aminoAcidRequirements = List.of(
                        new AminoAcidRequirement(null, "Lysin", 30.0),
                        new AminoAcidRequirement(null, "Leucin", 39.0),
                        new AminoAcidRequirement(null, "Isoleucin", 20.0),
                        new AminoAcidRequirement(null, "Valin", 26.0),
                        new AminoAcidRequirement(null, "Methionin", 15.0),
                        new AminoAcidRequirement(null, "Phenylalanin", 25.0),
                        new AminoAcidRequirement(null, "Threonin", 15.0),
                        new AminoAcidRequirement(null, "Tryptophan", 4.0),
                        new AminoAcidRequirement(null, "Histidin", 10.0),
                        new AminoAcidRequirement(null, "Glycin", 35.0) // Erhöht für Anti-Aging
                );
                aminoAcidRequirementRepository.saveAll(aminoAcidRequirements);

                logger.info("Amino acid requirements gespeichert.");
            }

        } catch (Exception e) {
            logger.error("Error during data initialization: ", e);
        }
    }
}