package com.example.NutritionTracker.decorator;

import java.util.Map;

public abstract class AminoAcidCalculatorDecorator implements AminoAcidCalculator {
    protected final AminoAcidCalculator wrapped;

    public AminoAcidCalculatorDecorator(AminoAcidCalculator wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Map<String, Double> calculateAminoAcids(Map<String, Double> dailyNeeds) {
        return wrapped.calculateAminoAcids(dailyNeeds);
    }
}