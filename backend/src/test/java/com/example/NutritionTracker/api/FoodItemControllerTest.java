package com.example.NutritionTracker.api;

import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.service.FoodItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@ExtendWith(MockitoExtension.class)  // Aktiviert Mockito für Tests
class FoodItemControllerTest {

    private MockMvc mockMvc;

    @Mock  // Erstellt eine Mock-Instanz des FoodItemService
    private FoodItemService foodItemService;

    @InjectMocks  // Initialisiert den FoodItemController mit gemocktem FoodItemService
    private FoodItemController foodItemController;

    @BeforeEach
    void setUp() {
        // MockMvc für Controller-Tests einrichten
        mockMvc = MockMvcBuilders.standaloneSetup(foodItemController).build();
    }

    @Test
    void shouldReturnAllFoodItems() throws Exception {
        // Mock-Daten erstellen
        List<FoodItem> mockFoodItems = List.of(new FoodItem(
                UUID.randomUUID(),
                "Apple",
                52.0,
                Map.of("Carbs", 14.0, "Protein", 0.3)
        ));

        // Verhalten des gemockten FoodItemService definieren
        when(foodItemService.getAllFoodItems()).thenReturn(mockFoodItems);

        // HTTP GET-Anfrage simulieren und Ergebnisse überprüfen
        mockMvc.perform(get("/api/food-items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Apple"));
    }
}