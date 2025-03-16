package com.example.NutritionTracker.decorator;

import java.util.Map;

/**
 * Interface for calculating amino acid requirements.
 * This serves as the base component for the Decorator Pattern,
 * allowing additional modifications for specific user needs.
 */
public interface AminoAcidCalculator {

    /**
     * Calculates the daily amino acid requirements based on the given input values.
     *
     * @param dailyNeeds A map containing the base amino acid requirements.
     * @return A map with the calculated amino acid values after modifications.
     */
    Map<String, Double> calculateAminoAcids(Map<String, Double> dailyNeeds);
}