package com.example.NutritionTracker.service;

import com.example.NutritionTracker.entity.NutritionLog;
import java.util.Map;

public interface AminoAcidCalculator {
    Map<String, Double> calculateAminoAcids(NutritionLog log);
}
