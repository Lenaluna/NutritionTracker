package com.example.NutritionTracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AminoAcidDTO {
    private String aminoAcid;
    private double value;
}