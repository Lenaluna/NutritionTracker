package com.example.NutritionTracker.api;

import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.service.FoodItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FoodItemControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FoodItemService foodItemService;

    @InjectMocks
    private FoodItemController foodItemController;

    private FoodItem foodItem1;
    private FoodItem foodItem2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(foodItemController).build();

        foodItem1 = new FoodItem(UUID.randomUUID(), "Apple", Map.of("Lysine", 0.2, "Methionine", 0.1));
        foodItem2 = new FoodItem(UUID.randomUUID(), "Banana", Map.of("Leucine", 0.3, "Histidine", 0.15));
    }

    @Test
    void shouldReturnAllFoodItems() throws Exception {
        List<FoodItem> mockFoodItems = Arrays.asList(foodItem1, foodItem2);

        when(foodItemService.getAllFoodItems()).thenReturn(mockFoodItems);

        mockMvc.perform(get("/food-items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Apple"))
                .andExpect(jsonPath("$[1].name").value("Banana"));
    }

    @Test
    void shouldUpdateFoodItem() throws Exception {
        UUID id = foodItem1.getId();
        FoodItem updatedFoodItem = new FoodItem(id, "Updated Apple", Map.of("Lysine", 0.4, "Methionine", 0.2));

        when(foodItemService.updateFoodItem(eq(id), any(FoodItem.class))).thenReturn(Optional.of(updatedFoodItem));

        mockMvc.perform(put("/food-items/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "name": "Updated Apple",
                                "aminoAcidProfile": {
                                    "Lysine": 0.4,
                                    "Methionine": 0.2
                                }
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Apple"));
    }

    @Test
    void shouldReturnFoodItemById() throws Exception {
        UUID id = foodItem1.getId();

        when(foodItemService.getFoodItemById(id)).thenReturn(Optional.of(foodItem1));

        mockMvc.perform(get("/food-items/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Apple"));
    }

    @Test
    void shouldReturnNotFoundForNonExistingFoodItem() throws Exception {
        UUID id = UUID.randomUUID();
        when(foodItemService.getFoodItemById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/food-items/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldAddNewFoodItem() throws Exception {
        UUID newId = UUID.randomUUID();
        FoodItem newFoodItem = new FoodItem(newId, "Orange", Map.of("Valine", 0.25, "Isoleucine", 0.2));

        when(foodItemService.saveFoodItem(any(FoodItem.class))).thenReturn(newFoodItem);

        mockMvc.perform(post("/food-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Orange",
                                    "aminoAcidProfile": {
                                        "Valine": 0.25,
                                        "Isoleucine": 0.2
                                    }
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Orange"));
    }

    @Test
    void shouldDeleteFoodItem() throws Exception {
        UUID id = foodItem1.getId();
        doNothing().when(foodItemService).deleteFoodItem(id);

        mockMvc.perform(delete("/food-items/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(foodItemService, times(1)).deleteFoodItem(id);
    }
}