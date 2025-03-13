package com.example.NutritionTracker.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionLogCreateDTO {
    private UUID userId; // Das ist alles, was das Frontend schicken muss
}