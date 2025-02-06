package com.example.NutritionTracker.service;

import com.example.NutritionTracker.entity.NutritionLog;
import java.util.Map;

public class AthleteAminoAcidDecorator extends AminoAcidCalculatorDecorator {
    public AthleteAminoAcidDecorator(AminoAcidCalculator wrapped) {
        super(wrapped);
    }
    @Override
    public Map<String, Double> calculateAminoAcids(NutritionLog log) {
        Map<String, Double> baseProfile = wrapped.calculateAminoAcids(log);
        if (log.getUser().getIsAthlete()) {
            baseProfile.replaceAll((aminoAcid, value) -> value * 1.2); // 20% mehr für Sportler
        }
        return baseProfile;
    }
}
