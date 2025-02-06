package com.example.NutritionTracker.service;

import com.example.NutritionTracker.entity.NutritionLog;

import java.util.Map;

public abstract class AminoAcidCalculatorDecorator implements AminoAcidCalculator {
    protected final AminoAcidCalculator wrapped;

    public AminoAcidCalculatorDecorator(AminoAcidCalculator wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Map<String, Double> calculateAminoAcids(NutritionLog log) {
        return wrapped.calculateAminoAcids(log);
    }
}

