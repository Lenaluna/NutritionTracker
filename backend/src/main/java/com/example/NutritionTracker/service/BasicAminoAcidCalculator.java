package com.example.NutritionTracker.service;

import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.entity.NutritionLogFoodItem;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class BasicAminoAcidCalculator implements AminoAcidCalculator {
    @Override
    public Map<String, Double> calculateAminoAcids(NutritionLog log) {
        if (log == null || log.getFoodItems() == null) {
            return Collections.emptyMap(); // Return empty map if log or foodItems are null
        }

        return log.getFoodItems().stream()
                .map(NutritionLogFoodItem::getFoodItem) // Access the associated FoodItem
                .filter(foodItem -> foodItem.getAminoAcidProfile() != null) // Ignore null profiles
                .flatMap(foodItem -> foodItem.getAminoAcidProfile().entrySet().stream()) // Extract amino acids
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Double::sum // Sum values if keys are duplicated
                ));
    }
}