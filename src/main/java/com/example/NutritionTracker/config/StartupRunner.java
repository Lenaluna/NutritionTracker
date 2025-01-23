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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class StartupRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(StartupRunner.class);

    private final FoodItemRepository foodItemRepository;
    private final UserRepository userRepository;
    private final NutritionLogService nutritionLogService;

    public StartupRunner(FoodItemRepository foodItemRepository, UserRepository userRepository, NutritionLogService nutritionLogService) {
        this.foodItemRepository = foodItemRepository;
        this.userRepository = userRepository;
        this.nutritionLogService = nutritionLogService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("StartupRunner executed: Loading initial data...");

        // Benutzer erstellen
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setName("John Doe");
        user1.setAge(30);
        user1.setWeight(75.0);
        user1.setIsAthlete(false);

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setName("Jane Athlete");
        user2.setAge(25);
        user2.setWeight(65.0);
        user2.setIsAthlete(true);

        userRepository.saveAll(List.of(user1, user2));
        logger.debug("Users initialized: {} and {}", user1.getName(), user2.getName());

        // Nahrungsmittel erstellen
        FoodItem chickenBreast = new FoodItem();
        chickenBreast.setId(UUID.randomUUID());
        chickenBreast.setName("Chicken Breast");
        chickenBreast.setProteinContent(31.0);
        chickenBreast.setAminoAcidProfile(Map.of(
                "Lysin", 2.5,
                "Valin", 3.0,
                "Isoleucin", 1.8
        ));

        FoodItem quinoa = new FoodItem();
        quinoa.setId(UUID.randomUUID());
        quinoa.setName("Quinoa");
        quinoa.setProteinContent(14.0);
        quinoa.setAminoAcidProfile(Map.of(
                "Lysin", 1.2,
                "Valin", 2.0,
                "Isoleucin", 1.5
        ));

        foodItemRepository.saveAll(List.of(chickenBreast, quinoa));
        logger.debug("Food items initialized: {}, {}", chickenBreast.getName(), quinoa.getName());

        // Ernährungstagebuch erstellen
        NutritionLog log = new NutritionLog();
        log.setFoodItems(List.of(chickenBreast, quinoa));
        log.setUser(user2);
        logger.debug("Nutrition log created for user: {}", user2.getName());

        // Dekorator für Aminosäuren-Berechnung anwenden
        AminoAcidCalculator calculator = new BasicAminoAcidCalculator();
        if (user2.getAge() < 18) {
            calculator = new ChildAminoAcidDecorator(calculator);
            logger.debug("ChildAminoAcidDecorator applied for user: {}", user2.getName());
        }

        if (user2.getIsAthlete()) {
            calculator = new AthleteAminoAcidDecorator(calculator);
            logger.debug("AthleteAminoAcidDecorator applied for user: {}", user2.getName());
        }

        // Aminosäurenprofil berechnen
        Map<String, Double> aminoAcids = calculator.calculateAminoAcids(log);
        logger.info("Amino acid profile calculated for {}: {}", user2.getName(), aminoAcids);

        // Tagesbedarf berechnen und abgleichen
        Map<String, Double> user1DailyNeeds = nutritionLogService.calculateDailyAminoAcidNeeds(user1);
        Map<String, Double> user2DailyNeeds = nutritionLogService.calculateDailyAminoAcidNeeds(user2);

        nutritionLogService.compareAminoAcidIntakeWithNeeds("John Doe", aminoAcids, user1DailyNeeds);
        nutritionLogService.compareAminoAcidIntakeWithNeeds("Jane Athlete", aminoAcids, user2DailyNeeds);

        logger.info("StartupRunner completed: Initial data loaded.");
    }
}
