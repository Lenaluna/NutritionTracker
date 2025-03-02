package com.example.NutritionTracker.integration;

import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.service.FoodItemService;
import com.example.NutritionTracker.service.NutritionLogService;
import com.example.NutritionTracker.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Integrationstest für die NutritionLog-Funktionalität.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class NutritionLogIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private FoodItemService foodItemService;

    @Autowired
    private NutritionLogService nutritionLogService;

    private User testUser;

    /**
     * Bereinigt und initialisiert die Datenbank vor jedem Testlauf.
     */
    @BeforeEach
    void setup() {
        cleanDatabase();

        // Test-Benutzer persistieren und für alle Tests verfügbar machen
        testUser = userService.createUser(User.builder()
                .name("Integration Test User")
                .age(25)
                .weight(80.0)
                .isAthlete(false)
                .build());
        assertNotNull(testUser, "Test User should not be null");
    }

    /**
     * Leert die gesamte Datenbank, um Tests immer mit einem konsistenten Zustand zu starten.
     */
    void cleanDatabase() {
        // Alle FoodItems entfernen
        foodItemService.getAllFoodItems()
                .forEach(item -> foodItemService.deleteFoodItem(item.getId()));

        // Alle Logs entfernen
        nutritionLogService.getAllLogs()
                .forEach(log -> nutritionLogService.deleteLog(log.getId()));

        // Alle Benutzer entfernen
        userService.getAllUsers()
                .forEach(user -> userService.deleteUser(user.getId()));
    }

    /**
     * Testet die Erstellung eines NutritionLogs und das Hinzufügen von FoodItems.
     */
    /**
     * Testet die Erstellung eines NutritionLogs und das Hinzufügen von FoodItems.
     */
    @Test
    void testCreateNutritionLogAndAddFoodItems() {
        // Sicherstellen, dass ein Benutzer existiert
        assertNotNull(testUser, "Test User should not be null");

        // 1. NutritionLog erstellen
        NutritionLog log = nutritionLogService.createLog(NutritionLog.builder()
                .user(testUser)
                .logDateTime(LocalDateTime.now())
                .build());

        // Überprüfen, ob NutritionLog korrekt erstellt wurde
        assertNotNull(log, "Created NutritionLog should not be null");
        assertNotNull(log.getId(), "Created NutritionLog ID should not be null");

        // 2. FoodItem erstellen
        FoodItem foodItem = foodItemService.saveFoodItem(FoodItem.builder()
                .name("Test Food Item")
                .aminoAcidProfile(Map.of(
                        "Lysine", 2.1,
                        "Methionine", 3.4
                ))
                .build());

        // Überprüfen, ob FoodItem korrekt erstellt wurde
        assertNotNull(foodItem, "FoodItem should not be null");
        assertNotNull(foodItem.getId(), "Created FoodItem ID should not be null");

        // 3. FoodItem zum NutritionLog hinzufügen
        nutritionLogService.addFoodItemToLog(log.getId(), foodItem.getId());

        // 4. NutritionLog abrufen und sicherstellen, dass das FoodItem hinzugefügt wurde
        NutritionLog updatedLog = nutritionLogService.getLogById(log.getId())
                .orElseThrow(() -> new EntityNotFoundException("NutritionLog not found"));

        // Sicherstellen, dass das NutritionLog das hinzugefügte FoodItem enthält
        assertNotNull(updatedLog.getFoodItems(), "NutritionLog FoodItems list should not be null");
        assertFalse(updatedLog.getFoodItems().isEmpty(), "NutritionLog should contain at least one FoodItem");

        boolean containsItem = updatedLog.getFoodItems().stream()
                .anyMatch(foodLogItem -> foodLogItem.getFoodItem().getId().equals(foodItem.getId()));
        assertTrue(containsItem, "NutritionLog should contain the added FoodItem");
    }


    /**
     * Testet, dass ein FoodItem zu einem nicht existierenden NutritionLog nicht hinzugefügt werden kann.
     */
    @Test
    void testAddFoodItemToNonExistingLog() {
        UUID nonExistingLogId = UUID.randomUUID();
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                nutritionLogService.addFoodItemToLog(nonExistingLogId, UUID.randomUUID()));
        assertEquals("NutritionLog not found", exception.getMessage());
    }

    /**
     * Testet, dass ein nicht existierendes FoodItem nicht zu einem NutritionLog hinzugefügt werden kann.
     */
    @Test
    void testAddNonExistingFoodItemToLog() {
        NutritionLog nutritionLog = NutritionLog.builder()
                .logDateTime(LocalDateTime.now())
                .user(testUser)
                .build();

        NutritionLog savedLog = nutritionLogService.createLog(nutritionLog);

        UUID nonExistingItemId = UUID.randomUUID();

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                nutritionLogService.addFoodItemToLog(savedLog.getId(), nonExistingItemId));
        assertEquals("FoodItem not found", exception.getMessage());
    }

    /**
     * Testet das Abrufen aller NutritionLogs.
     */
    @Test
    void testFetchAllNutritionLogs() {
        NutritionLog log1 = NutritionLog.builder().logDateTime(LocalDateTime.now()).user(testUser).build();
        NutritionLog log2 = NutritionLog.builder().logDateTime(LocalDateTime.now().minusDays(1)).user(testUser).build();

        nutritionLogService.createLog(log1);
        nutritionLogService.createLog(log2);

        var logs = nutritionLogService.getAllLogs();

        assertNotNull(logs);
        assertEquals(2, logs.size());
    }

    /**
     * Testet die Aktualisierung eines NutritionLogs.
     */
    @Test
    void testUpdateNutritionLog() {
        NutritionLog log = NutritionLog.builder().logDateTime(LocalDateTime.now()).user(testUser).build();
        NutritionLog savedLog = nutritionLogService.createLog(log);

        assertNotNull(savedLog.getId());

        // Aktualisierung durchführen
        savedLog.setLogDateTime(LocalDateTime.now().minusDays(3));
        NutritionLog updatedLog = nutritionLogService.updateLog(savedLog.getId(), savedLog);

        assertEquals(savedLog.getId(), updatedLog.getId());
        assertEquals(savedLog.getLogDateTime(), updatedLog.getLogDateTime());
    }

    /**
     * Testet das Löschen eines NutritionLogs.
     */
    @Test
    void testDeleteNutritionLog() {
        NutritionLog log = NutritionLog.builder().logDateTime(LocalDateTime.now()).user(testUser).build();
        NutritionLog savedLog = nutritionLogService.createLog(log);

        assertNotNull(savedLog.getId());

        // Log löschen
        nutritionLogService.deleteLog(savedLog.getId());

        var logs = nutritionLogService.getAllLogs();
        assertTrue(logs.isEmpty());
    }
}