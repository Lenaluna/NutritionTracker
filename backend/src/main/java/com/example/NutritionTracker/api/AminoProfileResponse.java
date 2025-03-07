package com.example.NutritionTracker.api;

import lombok.Data;
import java.util.Map;

@Data
public class AminoProfileResponse {
    private Map<String, Double> aminoAcids;
    private Map<String, Double> dailyCoverage;
}