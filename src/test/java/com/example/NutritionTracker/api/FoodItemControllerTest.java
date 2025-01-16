package com.example.NutritionTracker.api;

import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.service.FoodItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class FoodItemControllerTest {

    @Mock
    private FoodItemService foodItemService;

    @InjectMocks
    private FoodItemController foodItemController;

    private MockMvc mockMvc;

    @Test
    void testGetAllFoodItems() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(foodItemController).build();

        when(foodItemService.getAllFoodItems()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/food-items"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testCreateFoodItem() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(foodItemController).build();

        FoodItem foodItem = new FoodItem();
        foodItem.setId(UUID.randomUUID());
        foodItem.setName("Apple");
        foodItem.setProteinContent(0.3);

        when(foodItemService.createFoodItem(any(FoodItem.class))).thenReturn(foodItem);

        mockMvc.perform(post("/api/food-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Apple\",\"proteinContent\":0.3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Apple"));
    }
}
