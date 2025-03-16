package com.example.NutritionTracker.dto;

import lombok.*;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) for representing a food item.
 * This DTO is used to transfer food item data between the backend and frontend.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FoodItemDTO {

    /** The unique identifier of the food item. */
    private UUID id;

    /** The name of the food item. */
    private String name;
}
