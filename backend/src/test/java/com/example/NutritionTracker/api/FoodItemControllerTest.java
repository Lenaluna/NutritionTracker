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

import java.util.List;
import java.util.Map;
import java.util.UUID;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FoodItemControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FoodItemService foodItemService;

    @InjectMocks
    private FoodItemController foodItemController;

    @BeforeEach
    void setUp() {

        mockMvc = MockMvcBuilders.standaloneSetup(foodItemController).build();
    }

    @Test
    void shouldReturnAllFoodItems() throws Exception {

        List<FoodItem> mockFoodItems = List.of(new FoodItem(
                UUID.randomUUID(),
                "Apple",
                52.0,
                Map.of("Carbs", 14.0, "Protein", 0.3)
        ));


        when(foodItemService.getAllFoodItems()).thenReturn(mockFoodItems);


        mockMvc.perform(get("/api/food-items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Apple"));
    }
}