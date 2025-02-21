package com.example.NutritionTracker.integration;

import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.repo.FoodItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class FoodItemIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @BeforeEach
    void clearDatabase() {
        foodItemRepository.deleteAll(); // Ensures a clean database state before each test
    }

    @Test
    @Transactional
    @Rollback(false)
    void shouldCreateAndRetrieveFoodItem() throws Exception {
        String requestBody = """
        {
            "name": "Banana",
            "aminoAcidProfile": {
                "Lysine": 1.2,
                "Valine": 2.0
            }
        }
        """;

        // Sends a POST request to create a new FoodItem
        mockMvc.perform(post("/api/food-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        // Verifies the FoodItem was persisted in the database
        List<FoodItem> items = foodItemRepository.findAll();
        assertFalse(items.isEmpty());
        assertEquals("Banana", items.get(0).getName());
    }

    @Test
    @Transactional
    @Rollback(false)
    void shouldRetrieveFoodItemById() throws Exception {
        // Creates and saves a FoodItem directly in the database
        FoodItem item = FoodItem.builder()
                .id(UUID.randomUUID())
                .name("Apple")
                .aminoAcidProfile(Map.of("Lysine", 0.5))
                .version(1)
                .build();
        foodItemRepository.save(item);

        // Sends a GET request to retrieve the FoodItem by ID
        mockMvc.perform(get("/api/food-items/{id}", item.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Apple"));
    }

    @Test
    @Transactional
    @Rollback(false)
    void shouldReturnNotFoundForInvalidId() throws Exception {
        UUID invalidId = UUID.randomUUID();

        // Sends a GET request for a non-existing FoodItem
        mockMvc.perform(get("/api/food-items/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @Rollback(false) // Ensures the delete transaction is committed before verification
    void shouldDeleteFoodItem() throws Exception {
        // Creates and saves a FoodItem that will be deleted
        FoodItem item = FoodItem.builder()
                .id(UUID.randomUUID())
                .name("Orange")
                .aminoAcidProfile(Map.of("Methionine", 0.8))
                .build();
        foodItemRepository.save(item);

        // Sends a DELETE request to remove the FoodItem
        mockMvc.perform(delete("/api/food-items/{id}", item.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verifies the FoodItem no longer exists in the database
        assertFalse(foodItemRepository.existsById(item.getId()));
    }
}