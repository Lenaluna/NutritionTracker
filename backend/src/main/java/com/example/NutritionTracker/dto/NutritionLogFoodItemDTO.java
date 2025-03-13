package com.example.NutritionTracker.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionLogFoodItemDTO {
    private UUID id;
    private UUID foodItemId; // Statt der gesamten FoodItem-Entität nur die ID
    private UUID nutritionLogId; // Statt der gesamten NutritionLog-Entität nur die ID
}