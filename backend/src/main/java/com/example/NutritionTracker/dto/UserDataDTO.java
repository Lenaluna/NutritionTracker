package com.example.NutritionTracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDataDTO {
    private String name;
    private double weight;
    private int age;
    private Boolean isAthlete;
    private Boolean isChild;
}