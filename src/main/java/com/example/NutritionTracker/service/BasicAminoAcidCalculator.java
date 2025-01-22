package com.example.NutritionTracker.service;

import com.example.NutritionTracker.entity.NutritionLog;
import java.util.Map;
import java.util.stream.Collectors;

public class BasicAminoAcidCalculator implements AminoAcidCalculator {
    @Override
    public Map<String, Double> calculateAminoAcids(NutritionLog log) {
        return log.getFoodItems().stream()
            .flatMap(item -> item.getAminoAcidProfile().entrySet().stream())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                Double::sum
            ));
    }
}
