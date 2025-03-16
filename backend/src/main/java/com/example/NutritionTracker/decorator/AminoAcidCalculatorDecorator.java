package com.example.NutritionTracker.decorator;

import java.util.Map;

/**
 * Abstract decorator class for modifying amino acid calculations.
 * This serves as a base class for specific decorators that adjust the amino acid requirements.
 * The decorator pattern allows multiple modifications to be applied dynamically.
 */
public abstract class AminoAcidCalculatorDecorator implements AminoAcidCalculator {
    protected final AminoAcidCalculator wrapped;

    /**
     * Constructs an {@code AminoAcidCalculatorDecorator} with the given wrapped calculator.
     *
     * @param wrapped The base {@code AminoAcidCalculator} instance to be decorated.
     */
    public AminoAcidCalculatorDecorator(AminoAcidCalculator wrapped) {
        this.wrapped = wrapped;
    }

    /**
     * Calculates amino acid needs by delegating the computation to the wrapped calculator.
     * Specific decorators will override this method to modify the calculation.
     *
     * @param dailyNeeds A map of amino acid requirements before modifications.
     * @return A modified map of amino acid needs.
     */
    @Override
    public Map<String, Double> calculateAminoAcids(Map<String, Double> dailyNeeds) {
        return wrapped.calculateAminoAcids(dailyNeeds);
    }
}