package com.example.NutritionTracker.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionLogResponseDTO {
    private UUID id;
    private UUID userId; // Nur die ID des Users, ohne FoodItems
}