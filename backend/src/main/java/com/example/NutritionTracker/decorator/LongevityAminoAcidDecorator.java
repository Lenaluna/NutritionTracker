package com.example.NutritionTracker.decorator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * The {@code LongevityAminoAcidDecorator} adjusts amino acid requirements to promote longevity.
 * This decorator applies specific increases and decreases to certain amino acids
 * based on research suggesting their impact on aging and overall health.
 */
public class LongevityAminoAcidDecorator extends AminoAcidCalculatorDecorator {

    private static final Logger logger = LoggerFactory.getLogger(LongevityAminoAcidDecorator.class);

    /**
     * Constructs a {@code LongevityAminoAcidDecorator} that wraps an existing {@link AminoAcidCalculator}.
     *
     * @param wrapped The base calculator that this decorator modifies.
     */
    public LongevityAminoAcidDecorator(AminoAcidCalculator wrapped) {
        super(wrapped);
    }

    /**
     * Modifies the base amino acid needs by applying longevity-based adjustments.
     * Certain amino acids are increased or reduced based on research related to aging.
     *
     * @param dailyNeeds The base daily amino acid requirements.
     * @return A modified map of amino acid requirements with longevity-focused adjustments.
     * @throws IllegalArgumentException If the provided daily needs map is {@code null}.
     * @throws IllegalStateException If the wrapped calculator returns a {@code null} result.
     */
    @Override
    public Map<String, Double> calculateAminoAcids(Map<String, Double> dailyNeeds) {
        if (dailyNeeds == null) {
            throw new IllegalArgumentException("Daily needs map cannot be null");
        }

        // Retrieve baseline amino acid values
        Map<String, Double> aminoAcids = super.calculateAminoAcids(dailyNeeds);

        if (aminoAcids == null) {
            throw new IllegalStateException("calculateAminoAcids() from wrapped returned null");
        }

        // Ensure all essential amino acids are present in the map
        String[] essentialAminoAcids = {
                "Lysin", "Phenylalanin", "Threonin", "Tryptophan",
                "Methionin", "Glycin", "Leucin", "Isoleucin", "Valin"
        };

        for (String aminoAcid : essentialAminoAcids) {
            aminoAcids.putIfAbsent(aminoAcid, 0.0);
        }

        // Apply longevity-based adjustments to specific amino acids
        aminoAcids.computeIfPresent("Methionin", (k, v) -> v * 0.8); // Reduce by 20%
        aminoAcids.computeIfPresent("Glycin", (k, v) -> v * 1.25); // Increase by 25%
        aminoAcids.computeIfPresent("Leucin", (k, v) -> v * 0.9); // Reduce by 10%
        aminoAcids.computeIfPresent("Isoleucin", (k, v) -> v * 0.9); // Reduce by 10%
        aminoAcids.computeIfPresent("Valin", (k, v) -> v * 0.9); // Reduce by 10%
        aminoAcids.computeIfPresent("Phenylalanin", (k, v) -> v * 1.2); // Increase by 20%
        aminoAcids.computeIfPresent("Threonin", (k, v) -> v * 0.95); // Reduce by 5%
        aminoAcids.computeIfPresent("Tryptophan", (k, v) -> v * 1.1); // Increase by 10%
        aminoAcids.computeIfPresent("Lysin", (k, v) -> v * 1.05); // Increase by 5%

        // Log the final calculated values for debugging purposes
        logger.info("Calculated amino acids for Longevity Decorator: {}", aminoAcids);

        return aminoAcids;
    }
}