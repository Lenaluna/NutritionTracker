package com.example.NutritionTracker.dto;

import com.example.NutritionTracker.entity.NutritionLogFoodItem;
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

    // Konstruktor zum Konvertieren von Entity zu DTO
    public NutritionLogFoodItemDTO(NutritionLogFoodItem nutritionLogFoodItem) {
        this.id = nutritionLogFoodItem.getId();
        this.foodItemId = nutritionLogFoodItem.getFoodItem().getId();
        this.nutritionLogId = nutritionLogFoodItem.getNutritionLog().getId();
    }
}