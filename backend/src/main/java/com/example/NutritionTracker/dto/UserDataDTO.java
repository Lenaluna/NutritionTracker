package com.example.NutritionTracker.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDataDTO {

    private UUID id;

    @NotBlank(message = "Der Name darf nicht leer sein")
    @Size(min = 3, max = 15, message = "Der Name muss zwischen 3 und 15 Zeichen lang sein")
    private String name;

    @NotNull(message = "Das Alter ist erforderlich")
    @Min(value = 3, message = "Das Alter muss mindestens 1 Jahr betragen")
    @Max(value = 120, message = "Das Alter darf maximal 120 Jahre sein")
    private Integer age;

    @NotNull(message = "Das Gewicht ist erforderlich")
    @DecimalMin(value = "12.0", message = "Das Gewicht muss mindestens 1 kg betragen")
    @DecimalMax(value = "500.0", message = "Das Gewicht darf maximal 500 kg betragen")
    private Double weight;

    private Boolean isAthlete;

    private Boolean isVegan;

    private Boolean isLongevityFocused;
}