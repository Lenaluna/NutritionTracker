package com.example.NutritionTracker.config;

import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.repo.FoodItemRepository;
import com.example.NutritionTracker.repo.UserRepository;
import com.example.NutritionTracker.service.AthleteAminoAcidDecorator;
import com.example.NutritionTracker.service.AminoAcidCalculator;
import com.example.NutritionTracker.service.BasicAminoAcidCalculator;
import com.example.NutritionTracker.service.NutritionLogService;
import com.example.NutritionTracker.service.ChildAminoAcidDecorator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Component
public class StartupRunner implements ApplicationRunner {

    private final FoodItemRepository foodItemRepository;
    private final UserRepository userRepository;
    private final NutritionLogService nutritionLogService;

    @Override
    public void run(ApplicationArguments args) {
        try {
            log.info("StartupRunner executed: Loading initial data...");

            // Check if the database already contains data
            if (userRepository.count() == 0 && foodItemRepository.count() == 0) {
                log.info("Database is empty, initializing data...");

                // Create users with required attributes
                User user1 = User.builder()
                        .id(UUID.randomUUID())
                        .name("John Doe")
                        .age(30)
                        .weight(75.0)
                        .isAthlete(false)
                        .build();

                User user2 = User.builder()
                        .id(UUID.randomUUID())
                        .name("Jane Athlete")
                        .age(25)
                        .weight(65.0)
                        .isAthlete(true)
                        .build();

                // Save users to the repository
                userRepository.saveAll(List.of(user1, user2));
                log.debug("Users initialized: {} and {}", user1.getName(), user2.getName());

                // Create food items with protein content and amino acid profiles
                FoodItem chickenBreast = FoodItem.builder()
                        .id(UUID.randomUUID())
                        .name("Chicken Breast")
                        .proteinContent(31.0)
                        .aminoAcidProfile(Map.of(
                                "Lysine", 2.5,
                                "Valine", 3.0,
                                "Isoleucine", 1.8
                        ))
                        .build();

                FoodItem quinoa = FoodItem.builder()
                        .id(UUID.randomUUID())
                        .name("Quinoa")
                        .proteinContent(14.0)
                        .aminoAcidProfile(Map.of(
                                "Lysine", 1.2,
                                "Valine", 2.0,
                                "Isoleucine", 1.5
                        ))
                        .build();

                // Save food items to the repository
                foodItemRepository.saveAll(List.of(chickenBreast, quinoa));
                log.debug("Food items initialized: {}, {}", chickenBreast.getName(), quinoa.getName());

                // Calculate total protein dynamically from the food items
                double totalProtein = Stream.of(chickenBreast, quinoa)
                        .mapToDouble(FoodItem::getProteinContent)
                        .sum();

                // Create a nutrition log entry for the user
                NutritionLog logEntry = NutritionLog.builder()
                        .user(user2)
                        .foodItems(List.of(chickenBreast, quinoa))
                        .logDate(LocalDate.now())
                        .totalProtein(totalProtein)
                        .build();

                log.debug("Nutrition log created for user: {}", user2.getName());

                // Apply decorators for amino acid calculations based on user attributes
                AminoAcidCalculator calculator = new BasicAminoAcidCalculator();

                // Apply child-specific decorator if the user is under 18
                if (user2.getAge() < 18) {
                    calculator = new ChildAminoAcidDecorator(calculator);
                    log.debug("ChildAminoAcidDecorator applied for user: {}", user2.getName());
                }

                // Apply athlete-specific decorator if the user is an athlete
                if (user2.getIsAthlete()) {
                    calculator = new AthleteAminoAcidDecorator(calculator);
                    log.debug("AthleteAminoAcidDecorator applied for user: {}", user2.getName());
                }

                // Calculate the amino acid profile for the nutrition log
                Map<String, Double> aminoAcids = calculator.calculateAminoAcids(logEntry);
                log.info("Amino acid profile calculated for {}: {}", user2.getName(), aminoAcids);

                // Calculate daily amino acid needs for each user
                Map<String, Double> user1DailyNeeds = nutritionLogService.calculateDailyAminoAcidNeeds(user1);
                Map<String, Double> user2DailyNeeds = nutritionLogService.calculateDailyAminoAcidNeeds(user2);

                // Compare amino acid intake with the user's daily needs
                nutritionLogService.compareAminoAcidIntakeWithNeeds("John Doe", aminoAcids, user1DailyNeeds);
                nutritionLogService.compareAminoAcidIntakeWithNeeds("Jane Athlete", aminoAcids, user2DailyNeeds);

                log.info("StartupRunner completed: Initial data loaded.");
            } else {
                log.info("Data already exists, skipping initialization.");
            }
        } catch (Exception e) {
            log.error("Error during initialization: ", e);
        }
    }
}
