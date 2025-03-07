package com.example.NutritionTracker.api;

import lombok.Data;
import java.util.List;
import java.util.UUID;

@Data
public class AminoProfileRequest {
    private UserData userData;            // Die User-Daten aus dem Frontend
    private List<UUID> selectedFoodIds;   // Liste der ausgewählten Lebensmittel-IDs
}