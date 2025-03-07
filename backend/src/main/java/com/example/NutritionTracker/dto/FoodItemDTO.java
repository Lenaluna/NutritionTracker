package com.example.NutritionTracker.dto;

import lombok.*;

import java.util.Map;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FoodItemDTO {
    private UUID id;
    private String name;
    private Map<String, Double> aminoAcidProfile;
}