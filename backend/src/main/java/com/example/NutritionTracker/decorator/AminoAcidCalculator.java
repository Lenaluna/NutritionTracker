package com.example.NutritionTracker.decorator;

import java.util.Map;

public interface AminoAcidCalculator {
    Map<String, Double> calculateAminoAcids(Map<String, Double> dailyNeeds);
}
