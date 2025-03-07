package com.example.NutritionTracker.api;

import com.example.NutritionTracker.dto.FoodItemDTO;
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

import java.util.*;

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

    private FoodItemDTO foodItem1;
    private FoodItemDTO foodItem2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(foodItemController).build();

        foodItem1 = new FoodItemDTO(UUID.randomUUID(), "Apple", Map.of("Lysine", 0.2, "Methionine", 0.1));
        foodItem2 = new FoodItemDTO(UUID.randomUUID(), "Banana", Map.of("Leucine", 0.3, "Histidine", 0.15));
    }

    @Test
    void shouldReturnAllFoodItems() throws Exception {
        List<FoodItemDTO> mockFoodItems = Arrays.asList(foodItem1, foodItem2);

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
        UUID id = UUID.randomUUID(); // Erzeuge eine UUID für den Test

        FoodItemDTO updatedFoodItemDTO = new FoodItemDTO(id, "Updated Apple",
                Map.of("Lysine", 0.4, "Methionine", 0.2));

        when(foodItemService.updateFoodItem(eq(id), any(FoodItemDTO.class)))
                .thenReturn(Optional.of(updatedFoodItemDTO));

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
        when(foodItemService.getFoodItemById(any())).thenReturn(Optional.of(foodItem1));

        mockMvc.perform(get("/food-items/{id}", "some-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Apple"));
    }

    @Test
    void shouldReturnNotFoundForNonExistingFoodItem() throws Exception {
        when(foodItemService.getFoodItemById(any())).thenReturn(Optional.empty());

        mockMvc.perform(get("/food-items/{id}", "some-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldAddNewFoodItem() throws Exception {
        UUID newId = UUID.randomUUID(); // Erzeuge eine UUID für das neue FoodItem

        FoodItemDTO newFoodItemDTO = new FoodItemDTO(newId, "Orange",
                Map.of("Valine", 0.25, "Isoleucine", 0.2));

        when(foodItemService.saveFoodItem(any(FoodItemDTO.class))).thenReturn(newFoodItemDTO);

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
        doNothing().when(foodItemService).deleteFoodItem(any());

        mockMvc.perform(delete("/food-items/{id}", "some-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(foodItemService, times(1)).deleteFoodItem(any());
    }
}