package com.example.NutritionTracker.dto;

import com.example.NutritionTracker.entity.NutritionLog;
import lombok.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionLogDTO {
    private UUID id;
    private UUID userId; // Statt der gesamten User-Entität nur die ID
    private List<NutritionLogFoodItemDTO> foodItems; // Vermeidet Lazy-Loading-Probleme

    public NutritionLogDTO(NutritionLog nutritionLog) {
        this.id = nutritionLog.getId();
        this.userId = nutritionLog.getUser().getId();
        this.foodItems = nutritionLog.getFoodItems().stream()
                .map(NutritionLogFoodItemDTO::new)
                .collect(Collectors.toList());
    }
}