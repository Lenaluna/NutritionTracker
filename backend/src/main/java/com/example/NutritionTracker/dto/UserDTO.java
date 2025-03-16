package com.example.NutritionTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) for user-related data.
 * This class is used to transfer user information without exposing the entire entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    /** The unique identifier of the user. */
    private UUID id;

    /** The user's name. */
    private String name;

    /** The user's age. */
    private Integer age;

    /** The user's weight in kilograms. */
    private Double weight;

    /** Indicates whether the user is an athlete. */
    private Boolean isAthlete;

    /** Indicates whether the user follows a vegan diet. */
    private Boolean isVegan;

    /** Indicates whether the user is focused on longevity-based nutrition. */
    private Boolean isLongevityFocused;
}