package com.example.NutritionTracker.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

/**
 * Data Transfer Object (DTO) for user data.
 * Contains user-related information with validation constraints.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDataDTO {

    /** The unique identifier of the user. */
    private UUID id;

    /** The user's name, required and must be between 3 and 15 characters. */
    @NotBlank(message = "Der Name darf nicht leer sein")
    @Size(min = 3, max = 15, message = "Der Name muss zwischen 3 und 15 Zeichen lang sein")
    private String name;

    /** The user's age, required and must be between 3 and 120 years. */
    @NotNull(message = "Das Alter ist erforderlich")
    @Min(value = 3, message = "Das Alter muss mindestens 1 Jahr betragen")
    @Max(value = 120, message = "Das Alter darf maximal 120 Jahre sein")
    private Integer age;

    /** The user's weight, required and must be between 12 kg and 500 kg. */
    @NotNull(message = "Das Gewicht ist erforderlich")
    @DecimalMin(value = "12.0", message = "Das Gewicht muss mindestens 1 kg betragen")
    @DecimalMax(value = "500.0", message = "Das Gewicht darf maximal 500 kg betragen")
    private Double weight;

    /** Indicates whether the user is an athlete. */
    private Boolean isAthlete;

    /** Indicates whether the user follows a vegan diet. */
    private Boolean isVegan;

    /** Indicates whether the user focuses on longevity-based nutrition. */
    private Boolean isLongevityFocused;
}