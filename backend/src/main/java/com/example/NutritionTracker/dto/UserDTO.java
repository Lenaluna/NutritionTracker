package com.example.NutritionTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

// DTO-Objekt für User
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private UUID id;
    private String name;
    private Integer age;
    private Double weight;
    private Boolean isAthlete;
}