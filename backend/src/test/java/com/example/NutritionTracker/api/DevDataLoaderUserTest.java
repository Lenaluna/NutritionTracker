package com.example.NutritionTracker.api;

import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.service.FoodItemService;
import com.example.NutritionTracker.service.NutritionLogService;
import com.example.NutritionTracker.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class DevDataLoaderUserTest {

    @Autowired
    private UserService userService;

    @Autowired
    private FoodItemService foodItemService;

    @Autowired
    private NutritionLogService nutritionLogService;

    private FoodItem chicken;
    private FoodItem quinoa;

    private User devUser;

    @BeforeEach
    void setup() {
        // Bereinige Datenbank, aber behalte den Nutzer aus dem DevDataLoader
        foodItemService.cleanup();
        nutritionLogService.cleanup();

        // DevDataLoader Nutzer abrufen
        devUser = userService.getAllUsers().stream().findFirst().orElseThrow(() ->
                new IllegalStateException("DevDataLoader User nicht gefunden!"));

        // Beispiel-FoodItems in die Datenbank einfügen
        chicken = foodItemService.saveFoodItem(FoodItem.builder()
                .name("Chicken")
                .aminoAcidProfile(Map.of("Leucine", 2.5, "Valine", 3.0))
                .build());

        quinoa = foodItemService.saveFoodItem(FoodItem.builder()
                .name("Quinoa")
                .aminoAcidProfile(Map.of("Leucine", 1.0, "Valine", 1.8))
                .build());
    }

    @Test
    void testAminoAcidCalculationsForDevDataLoaderUser() {
        // NutritionLog für den DevDataLoader Nutzer erstellen
        NutritionLog devLog = nutritionLogService.createLog(NutritionLog.builder()
                .user(devUser)
                .build());

        // Lebensmittel hinzufügen
        nutritionLogService.addFoodItemToLog(devLog.getId(), chicken.getId());
        nutritionLogService.addFoodItemToLog(devLog.getId(), quinoa.getId());

        // Aminosäuren berechnen
        Map<String, Double> devAminoAcids = nutritionLogService.calculateAminoAcidsForLog(devLog);

        // Berechnungen prüfen
        // Beispiel: keine Änderungen, da der Nutzer nicht speziell behandelt (Basic-User als Annahme)
        assertEquals(3.5, devAminoAcids.get("Leucine"), 0.01);
        assertEquals(4.8, devAminoAcids.get("Valine"), 0.01);
    }
}