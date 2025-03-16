package com.example.NutritionTracker.decorator;

import java.util.Map;

/**
 * The {@code VeganAminoAcidDecorator} adjusts amino acid requirements to account for common deficiencies
 * in plant-based diets. It increases the intake of key amino acids that are often limited in vegan protein sources.
 * The adjustments are based on nutritional needs for individuals following a plant-based diet.
 */
public class VeganAminoAcidDecorator extends AminoAcidCalculatorDecorator {

    /**
     * Constructs a {@code VeganAminoAcidDecorator} that wraps an existing {@link AminoAcidCalculator}.
     *
     * @param wrapped The base calculator that this decorator modifies.
     */
    public VeganAminoAcidDecorator(AminoAcidCalculator wrapped) {
        super(wrapped);
    }

    /**
     * Modifies the base amino acid needs by increasing essential amino acids that are often lacking in a vegan diet.
     *
     * @param dailyNeeds The base daily amino acid requirements.
     * @return A modified map of amino acid requirements with adjustments for a vegan diet.
     */
    @Override
    public Map<String, Double> calculateAminoAcids(Map<String, Double> dailyNeeds) {
        Map<String, Double> aminoAcids = super.calculateAminoAcids(dailyNeeds);

        // Increase essential amino acids that are commonly limited in plant-based diets
        aminoAcids.computeIfPresent("Lysin", (k, v) -> v * 1.25);       // +25% (often limited in grains)
        aminoAcids.computeIfPresent("Methionin", (k, v) -> v * 1.2);   // +20% (often limited in legumes)
        aminoAcids.computeIfPresent("Tryptophan", (k, v) -> v * 1.15); // +15% (important for serotonin production)
        aminoAcids.computeIfPresent("Threonin", (k, v) -> v * 1.15);   // +15% (needed for balanced protein synthesis)
        aminoAcids.computeIfPresent("Leucin", (k, v) -> v * 1.1);      // +10% (crucial for muscle protein synthesis)
        aminoAcids.computeIfPresent("Histidin", (k, v) -> v * 1.1);    // +10% (essential for growth and tissue repair)
        aminoAcids.computeIfPresent("Isoleucin", (k, v) -> v * 1.05);  // +5% (supports muscle recovery)
        aminoAcids.computeIfPresent("Valin", (k, v) -> v * 1.1);       // +10% (important for muscle metabolism)

        // Additional adjustments based on common plant-based diet needs
        aminoAcids.computeIfPresent("Phenylalanin", (k, v) -> v * 1.2); // +20% (predecessor for neurotransmitters)
        aminoAcids.computeIfPresent("Cystein", (k, v) -> v * 1.1);      // +10% (supports antioxidant functions)
        aminoAcids.computeIfPresent("Glycin", (k, v) -> v * 1.2);       // +20% (supports collagen production)

        return aminoAcids;
    }
}