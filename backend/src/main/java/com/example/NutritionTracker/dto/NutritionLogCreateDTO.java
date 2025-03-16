package com.example.NutritionTracker.dto;

import lombok.*;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) for creating a new NutritionLog.
 * This DTO is used by the frontend to send only the user ID to the backend,
 * indicating that a new NutritionLog should be created for this user.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionLogCreateDTO {

    /** The unique identifier of the user for whom the NutritionLog is created. */
    private UUID userId;
}