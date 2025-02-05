package com.example.NutritionTracker.config;

import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.repo.FoodItemRepository;
import com.example.NutritionTracker.repo.NutritionLogRepository;
import com.example.NutritionTracker.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class StartupRunner implements ApplicationRunner {

    private final UserRepository userRepository;
    private final FoodItemRepository foodItemRepository;
    private final NutritionLogRepository nutritionLogRepository;

    @Override
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0 || foodItemRepository.count() > 0 || nutritionLogRepository.count() > 0) {
            log.info("Database already initialized. Skipping data loading.");
            return;
        }

        log.info("Initializing the database with test data...");

        User user1 = userRepository.save(User.builder()
                .name("John Doe")
                .age(30)
                .weight(75.0)
                .isAthlete(false)
                .build());

        User user2 = userRepository.save(User.builder()
                .name("Jane Athlete")
                .age(25)
                .weight(65.0)
                .isAthlete(true)
                .build());

        log.info("Users initialized: {}, {}", user1.getName(), user2.getName());

        FoodItem chicken = foodItemRepository.save(FoodItem.builder()
                .name("Chicken Breast")
                .proteinContent(31.0)
                .aminoAcidProfile(Map.of(
                        "Lysine", 2.5,
                        "Valine", 3.0,
                        "Isoleucine", 1.8
                ))
                .build());

        FoodItem quinoa = foodItemRepository.save(FoodItem.builder()
                .name("Quinoa")
                .proteinContent(14.0)
                .aminoAcidProfile(Map.of(
                        "Lysine", 1.2,
                        "Valine", 2.0,
                        "Isoleucine", 1.5
                ))
                .build());

        log.info("Food items initialized: {}, {}", chicken.getName(), quinoa.getName());

        user1 = userRepository.findById(user1.getId()).orElseThrow();
        user2 = userRepository.findById(user2.getId()).orElseThrow();

        chicken = foodItemRepository.findById(chicken.getId()).orElseThrow();
        quinoa = foodItemRepository.findById(quinoa.getId()).orElseThrow();

        nutritionLogRepository.save(NutritionLog.builder()
                .user(user1)
                .foodItems(List.of(chicken))
                .logDate(LocalDate.now())
                .totalProtein(chicken.getProteinContent())
                .build());

        nutritionLogRepository.save(NutritionLog.builder()
                .user(user2)
                .foodItems(List.of(chicken, quinoa))
                .logDate(LocalDate.now())
                .totalProtein(chicken.getProteinContent() + quinoa.getProteinContent())
                .build());

        log.info("Nutrition logs initialized for users: {}, {}", user1.getName(), user2.getName());
        log.info("Database setup complete!");
    }
}