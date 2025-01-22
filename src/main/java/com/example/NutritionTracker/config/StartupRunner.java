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
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class StartupRunner implements ApplicationRunner {

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
        System.out.println("StartupRunner executed: Loading initial data...");

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

        // Ernährungstagebuch erstellen
        NutritionLog log = new NutritionLog();
        log.setFoodItems(List.of(chickenBreast, quinoa));
        log.setUser(user2); // Beispiel: Jane Athlete ist der Benutzer des Logs

        // Dekorator für Aminosäuren-Berechnung anwenden
        AminoAcidCalculator calculator = new BasicAminoAcidCalculator();
        // Prüfen, ob der Benutzer ein Kind ist (z. B. Alter < 18)
        if (user2.getAge() < 18) {
            calculator = new ChildAminoAcidDecorator(calculator);
        }

        // Athleten-Dekorator hinzufügen, wenn Benutzer ein Athlet ist
        if (user2.getIsAthlete()) {
            calculator = new AthleteAminoAcidDecorator(calculator);
        }

        // Aminosäurenprofil berechnen
        Map<String, Double> aminoAcids = calculator.calculateAminoAcids(log);

        // Tagesbedarf berechnen und abgleichen
        Map<String, Double> user1DailyNeeds = nutritionLogService.calculateDailyAminoAcidNeeds(user1);
        Map<String, Double> user2DailyNeeds = nutritionLogService.calculateDailyAminoAcidNeeds(user2);

        nutritionLogService.compareAminoAcidIntakeWithNeeds("John Doe", aminoAcids, user1DailyNeeds);
        nutritionLogService.compareAminoAcidIntakeWithNeeds("Jane Athlete", aminoAcids, user2DailyNeeds);

        System.out.println("StartupRunner completed: Initial data loaded.");
    }
}
