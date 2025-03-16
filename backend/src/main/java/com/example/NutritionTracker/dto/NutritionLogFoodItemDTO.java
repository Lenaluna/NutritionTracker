package com.example.NutritionTracker.dto;

import com.example.NutritionTracker.entity.NutritionLogFoodItem;
import lombok.*;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) for transferring NutritionLogFoodItem data.
 * This DTO avoids Lazy-Loading issues by including only necessary data.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionLogFoodItemDTO {

    /** The unique identifier of the NutritionLogFoodItem entry. */
    private UUID id;

    /** The unique identifier of the associated FoodItem (instead of the full FoodItem entity). */
    private UUID foodItemId;

    /** The unique identifier of the associated NutritionLog (instead of the full NutritionLog entity). */
    private UUID nutritionLogId;

    /**
     * Constructs a NutritionLogFoodItemDTO from a given NutritionLogFoodItem entity.
     * Extracts only relevant data (IDs) to keep the DTO lightweight.
     *
     * @param nutritionLogFoodItem The NutritionLogFoodItem entity to convert into a DTO.
     */
    public NutritionLogFoodItemDTO(NutritionLogFoodItem nutritionLogFoodItem) {
        this.id = nutritionLogFoodItem.getId();
        this.foodItemId = nutritionLogFoodItem.getFoodItem().getId();
        this.nutritionLogId = nutritionLogFoodItem.getNutritionLog().getId();
    }
}