package com.example.NutritionTracker.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionLogDTO {
    private UUID id;
    private UUID userId; // Statt der gesamten User-Entität nur die ID
    private List<NutritionLogFoodItemDTO> foodItems; // Vermeidet Lazy-Loading-Probleme
}