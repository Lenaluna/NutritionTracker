package com.example.NutritionTracker.integration;

import com.example.NutritionTracker.Application;
import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.repo.FoodItemRepository;
import com.example.NutritionTracker.repo.NutritionLogRepository;
import com.example.NutritionTracker.repo.UserRepository;
import com.example.NutritionTracker.service.NutritionLogService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class FoodItemIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private NutritionLogRepository nutritionLogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NutritionLogService nutritionLogService;

    private User testUser;

    @Transactional
    @Test
    public void testFoodItemNameGetter() {
        FoodItem item = new FoodItem();
        item.setName("TestName");
        Assertions.assertEquals("TestName", item.getName());
    }

    @Transactional
    @BeforeEach
    void setUp() {
        // Clear all relevant repositories to ensure a clean database state before each test
        nutritionLogRepository.deleteAll();
        foodItemRepository.deleteAll();
        userRepository.deleteAll();
        nutritionLogRepository.flush();
        foodItemRepository.flush();
        userRepository.flush();

        // Create a test user for testing
        testUser = User.builder()
                .name("Test User")
                .age(30)
                .weight(70.0)
                .isAthlete(false)
                .nutritionLogs(new ArrayList<>())
                .build();
        testUser = userRepository.save(testUser);

        // Add some default FoodItems to the database
        FoodItem apple = FoodItem.builder()
                .id(UUID.randomUUID())
                .name("Apple")
                .aminoAcidProfile(Map.of("Vitamin C", 12.0, "Iron", 0.1))
                .build();

        FoodItem banana = FoodItem.builder()
                .id(UUID.randomUUID())
                .name("Banana")
                .aminoAcidProfile(Map.of("Potassium", 23.0, "Magnesium", 4.0))
                .build();

        foodItemRepository.saveAll(List.of(apple, banana));
    }

    @Test
    @Transactional
    @Rollback
    void shouldRetrieveFoodItemsFromDatabase() throws Exception {
        // Arrange: Add a new FoodItem to the database
        FoodItem foodItem = FoodItem.builder()
                .id(UUID.randomUUID())
                .name("Test Food")
                .aminoAcidProfile(Map.of("Vitamin C", 50.0))
                .build();
        foodItemRepository.save(foodItem);

        // Act & Assert: Perform API call to retrieve the FoodItem and validate response
        mockMvc.perform(get("/api/food-items/{id}", foodItem.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Test Food")))
                .andExpect(jsonPath("$.aminoAcidProfile['Vitamin C']", is(50.0)));
    }

    @Test
    @Transactional
    @Rollback
    void shouldAddFoodItemToNutritionLog() throws Exception {
        // Arrange: Create a NutritionLog and save it to the database
        NutritionLog nutritionLog = NutritionLog.builder()
                .id(UUID.randomUUID())
                .user(testUser)
                .foodItems(new ArrayList<>())
                .build();
        nutritionLog = nutritionLogRepository.save(nutritionLog);

        // Add a new FoodItem to the database
        FoodItem foodItem = FoodItem.builder()
                .id(UUID.randomUUID())
                .name("Test Food")
                .aminoAcidProfile(Map.of("Vitamin C", 20.0))
                .build();
        foodItem = foodItemRepository.save(foodItem);

        // Act: Add the FoodItem to the NutritionLog using the service
        nutritionLogService.addFoodItemToLog(nutritionLog.getId(), foodItem.getId());

        // Assert: Verify that the FoodItem was correctly added to the NutritionLog
        Optional<NutritionLog> updatedLog = nutritionLogRepository.findById(nutritionLog.getId());
        assertTrue(updatedLog.isPresent(), "NutritionLog should exist");
        assertFalse(updatedLog.get().getFoodItems().isEmpty(), "FoodItems list should not be empty");

        // Use a final reference for the lambda expression
        FoodItem finalFoodItem = foodItem;
        assertTrue(updatedLog.get().getFoodItems().stream()
                        .anyMatch(logItem -> logItem.getFoodItem().getId().equals(finalFoodItem.getId())),
                "FoodItem should be linked to the NutritionLog");
    }

    @Test
    @Transactional
    @Rollback
    void shouldRemoveFoodItemFromNutritionLog() throws Exception {
        // Arrange: Create a NutritionLog and associate it with a FoodItem
        NutritionLog nutritionLog = NutritionLog.builder()
                .id(UUID.randomUUID())
                .user(testUser)
                .foodItems(new ArrayList<>())
                .build();
        nutritionLog = nutritionLogRepository.save(nutritionLog);

        FoodItem foodItem = FoodItem.builder()
                .id(UUID.randomUUID())
                .name("Test Food")
                .aminoAcidProfile(null)
                .build();
        foodItem = foodItemRepository.save(foodItem);

        // Add the FoodItem to the NutritionLog
        nutritionLogService.addFoodItemToLog(nutritionLog.getId(), foodItem.getId());

        // Act: Remove the FoodItem from the NutritionLog via the API
        mockMvc.perform(delete("/api/nutrition-logs/{logId}/food-item/{foodItemId}",
                        nutritionLog.getId(), foodItem.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Assert: Verify that the FoodItem is no longer associated with the NutritionLog
        Optional<NutritionLog> updatedLog = nutritionLogRepository.findById(nutritionLog.getId());
        assertTrue(updatedLog.isPresent(), "NutritionLog should exist");

        // Use a final reference for the lambda expression
        FoodItem finalFoodItem = foodItem;
        assertFalse(updatedLog.get().getFoodItems().stream()
                        .anyMatch(logItem -> logItem.getFoodItem().getId().equals(finalFoodItem.getId())),
                "FoodItem should be removed from the NutritionLog");
    }
}