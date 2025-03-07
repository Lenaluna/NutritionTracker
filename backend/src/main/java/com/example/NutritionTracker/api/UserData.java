package com.example.NutritionTracker.api;

import lombok.Data;

@Data
public class UserData {
    private String name;
    private Double weight;
    private Integer age;
    private Boolean isAthlete;
    private Boolean isChild;
}