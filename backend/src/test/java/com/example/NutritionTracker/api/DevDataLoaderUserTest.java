package com.example.NutritionTracker.api;

import com.example.NutritionTracker.dto.FoodItemDTO;
import com.example.NutritionTracker.dto.UserDTO;
import com.example.NutritionTracker.entity.NutritionLog;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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

    private FoodItemDTO chicken;
    private FoodItemDTO quinoa;
    private UserDTO devUser;
    private NutritionLog devLog;

    @BeforeEach
    void setup() {
        foodItemService.cleanup();
        nutritionLogService.cleanup();

        // DevDataLoader User abrufen oder erstellen
        devUser = userService.getUser().orElseGet(() ->
                userService.saveOrUpdateUser(new UserDTO(UUID.randomUUID(), "Test User", 30, 70.0, false)));

        // Beispielhafte FoodItems hinzufügen
        chicken = foodItemService.saveFoodItem(new FoodItemDTO(UUID.randomUUID(), "Chicken", Map.of("Leucine", 2.5, "Valine", 3.0)));
        quinoa = foodItemService.saveFoodItem(new FoodItemDTO(UUID.randomUUID(), "Quinoa", Map.of("Leucine", 1.0, "Valine", 1.8)));

        // NutritionLog für den User erstellen
        devLog = nutritionLogService.createLog(NutritionLog.builder()
                .user(userService.getUserById(devUser.getId()))
                .build());
    }

    @Test
    void testAminoAcidCalculationsForDevDataLoaderUser() {
        // Lebensmittel hinzufügen
        nutritionLogService.addFoodItemToLog(devLog.getId(), chicken.getId());
        nutritionLogService.addFoodItemToLog(devLog.getId(), quinoa.getId());

        // **Fix: Hier übergeben wir jetzt die UUID statt dem NutritionLog-Objekt**
        Map<String, Double> devAminoAcids = nutritionLogService.calculateAminoAcidsForLog(devLog.getId());

        // Berechnungen überprüfen
        assertEquals(3.5, devAminoAcids.get("Leucine"), 0.01);
        assertEquals(4.8, devAminoAcids.get("Valine"), 0.01);
    }
}