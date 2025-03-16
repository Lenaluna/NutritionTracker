package com.example.NutritionTracker.dto;

import com.example.NutritionTracker.entity.NutritionLog;
import lombok.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Data Transfer Object (DTO) for transferring NutritionLog data.
 * This DTO helps prevent Lazy-Loading issues by only including necessary data.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionLogDTO {

    /** The unique identifier of the NutritionLog. */
    private UUID id;

    /** The unique identifier of the associated user (instead of the full user entity). */
    private UUID userId;

    /** List of food items in the NutritionLog to avoid Lazy-Loading issues. */
    private List<NutritionLogFoodItemDTO> foodItems;

    /**
     * Constructs a NutritionLogDTO from a given NutritionLog entity.
     * Extracts only relevant data (ID, user ID, and food items) to keep the DTO lightweight.
     *
     * @param nutritionLog The NutritionLog entity from which to create the DTO.
     */
    public NutritionLogDTO(NutritionLog nutritionLog) {
        this.id = nutritionLog.getId();
        this.userId = nutritionLog.getUser().getId();
        this.foodItems = nutritionLog.getFoodItems().stream()
                .map(NutritionLogFoodItemDTO::new)
                .collect(Collectors.toList());
    }
}