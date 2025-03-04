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
 * Integration test for the NutritionLog functionality.
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
     * Cleans and initializes the database before each test run.
     */
    @BeforeEach
    void setup() {
        cleanDatabase();

        // Persist test user and make it available for all tests
        testUser = userService.createUser(User.builder()
                .name("Integration Test User")
                .age(25)
                .weight(80.0)
                .isAthlete(false)
                .build());
        assertNotNull(testUser, "Test User should not be null");
    }

    /**
     * Empties the entire database to start tests with a consistent state.
     */
    void cleanDatabase() {
        // Remove all FoodItems
        foodItemService.getAllFoodItems()
                .forEach(item -> foodItemService.deleteFoodItem(item.getId()));

        // Remove all logs
        nutritionLogService.getAllLogs()
                .forEach(log -> nutritionLogService.deleteLog(log.getId()));

        // Remove all users
        userService.getAllUsers()
                .forEach(user -> userService.deleteUser(user.getId()));
    }

    /**
     * Tests the creation of a NutritionLog and adding FoodItems to it.
     */
    @Test
    void testCreateNutritionLogAndAddFoodItems() {
        // Ensure a user exists
        assertNotNull(testUser, "Test User should not be null");

        // 1. Create a NutritionLog
        NutritionLog log = nutritionLogService.createLog(NutritionLog.builder()
                .user(testUser)
                .logDateTime(LocalDateTime.now())
                .build());

        // Verify that the NutritionLog was created correctly
        assertNotNull(log, "Created NutritionLog should not be null");
        assertNotNull(log.getId(), "Created NutritionLog ID should not be null");

        // 2. Create a FoodItem
        FoodItem foodItem = foodItemService.saveFoodItem(FoodItem.builder()
                .name("Test Food Item")
                .aminoAcidProfile(Map.of(
                        "Lysine", 2.1,
                        "Methionine", 3.4
                ))
                .build());

        // Verify that the FoodItem was created correctly
        assertNotNull(foodItem, "FoodItem should not be null");
        assertNotNull(foodItem.getId(), "Created FoodItem ID should not be null");

        // 3. Add the FoodItem to the NutritionLog
        nutritionLogService.addFoodItemToLog(log.getId(), foodItem.getId());

        // 4. Retrieve the NutritionLog and ensure the FoodItem was added
        NutritionLog updatedLog = nutritionLogService.getLogById(log.getId())
                .orElseThrow(() -> new EntityNotFoundException("NutritionLog not found"));

        // Ensure that the NutritionLog contains the added FoodItem
        assertNotNull(updatedLog.getFoodItems(), "NutritionLog FoodItems list should not be null");
        assertFalse(updatedLog.getFoodItems().isEmpty(), "NutritionLog should contain at least one FoodItem");

        boolean containsItem = updatedLog.getFoodItems().stream()
                .anyMatch(foodLogItem -> foodLogItem.getFoodItem().getId().equals(foodItem.getId()));
        assertTrue(containsItem, "NutritionLog should contain the added FoodItem");
    }

    /**
     * Tests that a FoodItem cannot be added to a non-existing NutritionLog.
     */
    @Test
    void testAddFoodItemToNonExistingLog() {
        UUID nonExistingLogId = UUID.randomUUID();
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                nutritionLogService.addFoodItemToLog(nonExistingLogId, UUID.randomUUID()));
        assertEquals("NutritionLog not found", exception.getMessage());
    }

    /**
     * Tests that a non-existing FoodItem cannot be added to a NutritionLog.
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
     * Tests retrieving all NutritionLogs.
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
     * Tests updating a NutritionLog.
     */
    @Test
    void testUpdateNutritionLog() {
        NutritionLog log = NutritionLog.builder().logDateTime(LocalDateTime.now()).user(testUser).build();
        NutritionLog savedLog = nutritionLogService.createLog(log);

        assertNotNull(savedLog.getId());

        // Perform update
        savedLog.setLogDateTime(LocalDateTime.now().minusDays(3));
        NutritionLog updatedLog = nutritionLogService.updateLog(savedLog.getId(), savedLog);

        assertEquals(savedLog.getId(), updatedLog.getId());
        assertEquals(savedLog.getLogDateTime(), updatedLog.getLogDateTime());
    }

    /**
     * Tests deleting a NutritionLog.
     */
    @Test
    void testDeleteNutritionLog() {
        NutritionLog log = NutritionLog.builder().logDateTime(LocalDateTime.now()).user(testUser).build();
        NutritionLog savedLog = nutritionLogService.createLog(log);

        assertNotNull(savedLog.getId());

        // Delete the log
        nutritionLogService.deleteLog(savedLog.getId());

        var logs = nutritionLogService.getAllLogs();
        assertTrue(logs.isEmpty());
    }
}