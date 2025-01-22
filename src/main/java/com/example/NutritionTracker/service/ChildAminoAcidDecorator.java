package com.example.NutritionTracker.service;

import com.example.NutritionTracker.entity.NutritionLog;

import java.util.Map;

public class ChildAminoAcidDecorator extends AminoAcidCalculatorDecorator {
    public ChildAminoAcidDecorator(AminoAcidCalculator wrapped) {
        super(wrapped);
    }

    @Override
    public Map<String, Double> calculateAminoAcids(NutritionLog log) {
        Map<String, Double> baseProfile = super.calculateAminoAcids(log);
        baseProfile.replaceAll((aminoAcid, value) -> value * 0.8); // Reduzierung um 20 %
        return baseProfile;
    }
}
