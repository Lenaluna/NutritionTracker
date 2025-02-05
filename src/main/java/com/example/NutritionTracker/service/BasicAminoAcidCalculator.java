package com.example.NutritionTracker.service;

import com.example.NutritionTracker.entity.NutritionLog;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class BasicAminoAcidCalculator implements AminoAcidCalculator {
    @Override
    public Map<String, Double> calculateAminoAcids(NutritionLog log) {
        if (log == null || log.getFoodItems() == null) {
            return Collections.emptyMap(); // Falls log oder foodItems null sind, geben wir eine leere Map zurück.
        }

        return log.getFoodItems().stream()
                .filter(item -> item.getAminoAcidProfile() != null) // Ignoriert Null-Werte für Aminosäureprofile
                .flatMap(item -> item.getAminoAcidProfile().entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Double::sum // Falls ein Schlüssel mehrfach vorkommt, summieren wir die Werte
                ));
    }
}