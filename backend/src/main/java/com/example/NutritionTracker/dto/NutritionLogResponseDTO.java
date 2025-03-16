package com.example.NutritionTracker.dto;

import lombok.*;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) for sending NutritionLog creation responses.
 * This DTO contains only the essential information to avoid unnecessary data transfer.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionLogResponseDTO {

    /** The unique identifier of the created NutritionLog. */
    private UUID id;

    /** The unique identifier of the associated User, without including FoodItems. */
    private UUID userId;
}