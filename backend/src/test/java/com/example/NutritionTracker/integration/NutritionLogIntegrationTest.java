package com.example.NutritionTracker.integration;

import com.example.NutritionTracker.dto.FoodItemDTO;
import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.service.FoodItemService;
import com.example.NutritionTracker.service.NutritionLogService;
import com.example.NutritionTracker.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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

    @BeforeEach
    void setup() {
        cleanDatabase();

        testUser = User.builder()
                .name("Test User")
                .age(25)
                .weight(80.0)
                .isAthlete(false)
                .build();

        testUser = userService.createUser(testUser);
        assertNotNull(testUser, "Test User should not be null");
    }

    void cleanDatabase() {
        foodItemService.getAllFoodItems()
                .forEach(item -> foodItemService.deleteFoodItem(item.getId()));

        nutritionLogService.getAllLogs()
                .forEach(log -> nutritionLogService.deleteLog(log.getId()));

        userService.getAllUsers()
                .forEach(user -> userService.deleteUser(user.getId()));
    }

    @Test
    void testCreateNutritionLogAndAddFoodItems() {
        assertNotNull(testUser, "Test User should not be null");

        NutritionLog log = NutritionLog.builder()
                .user(testUser)
                .logDateTime(LocalDateTime.now())
                .build();

        log = nutritionLogService.createLog(log);
        assertNotNull(log.getId(), "Created NutritionLog ID should not be null");

        FoodItemDTO foodItem = new FoodItemDTO(UUID.randomUUID(), "Test Food Item",
                Map.of("Lysine", 2.1, "Methionine", 3.4));

        UUID foodItemId = foodItemService.saveFoodItem(foodItem).getId();

        nutritionLogService.addFoodItemToLog(log.getId(), foodItemId);

        NutritionLog updatedLog = nutritionLogService.getLogById(log.getId())
                .orElseThrow(() -> new EntityNotFoundException("NutritionLog not found"));

        assertNotNull(updatedLog.getFoodItems(), "NutritionLog FoodItems list should not be null");
        assertFalse(updatedLog.getFoodItems().isEmpty(), "NutritionLog should contain at least one FoodItem");

        boolean containsItem = updatedLog.getFoodItems().stream()
                .anyMatch(foodLogItem -> foodLogItem.getFoodItem().getId().equals(foodItemId));
        assertTrue(containsItem, "NutritionLog should contain the added FoodItem");
    }

    @Test
    void testAddFoodItemToNonExistingLog() {
        UUID nonExistingLogId = UUID.randomUUID();
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                nutritionLogService.addFoodItemToLog(nonExistingLogId, UUID.randomUUID()));
        assertEquals("NutritionLog not found", exception.getMessage());
    }

    @Test
    void testAddNonExistingFoodItemToLog() {
        NutritionLog savedLog = nutritionLogService.createLog(NutritionLog.builder()
                .logDateTime(LocalDateTime.now())
                .user(testUser)
                .build());

        UUID nonExistingItemId = UUID.randomUUID();

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                nutritionLogService.addFoodItemToLog(savedLog.getId(), nonExistingItemId));
        assertEquals("FoodItem not found", exception.getMessage());
    }

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

    @Test
    void testUpdateNutritionLog() {
        NutritionLog savedLog = nutritionLogService.createLog(NutritionLog.builder()
                .logDateTime(LocalDateTime.now())
                .user(testUser)
                .build());

        assertNotNull(savedLog.getId());

        savedLog.setLogDateTime(LocalDateTime.now().minusDays(3));
        NutritionLog updatedLog = nutritionLogService.updateLog(savedLog.getId(), savedLog);

        assertEquals(savedLog.getId(), updatedLog.getId());
        assertEquals(savedLog.getLogDateTime(), updatedLog.getLogDateTime());
    }

    @Test
    void testDeleteNutritionLog() {
        NutritionLog savedLog = nutritionLogService.createLog(NutritionLog.builder()
                .logDateTime(LocalDateTime.now())
                .user(testUser)
                .build());

        assertNotNull(savedLog.getId());

        nutritionLogService.deleteLog(savedLog.getId());

        var logs = nutritionLogService.getAllLogs();
        assertTrue(logs.isEmpty());
    }
}