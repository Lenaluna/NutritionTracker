package com.example.NutritionTracker.decorator;

import java.util.List;
import java.util.Map;

/**
 * Decorator class for adjusting amino acid requirements for athletes.
 * Increases Branched-Chain Amino Acids (BCAAs) and other essential amino acids
 * to reflect higher protein needs in physically active individuals.
 */
public class AthleteAminoAcidDecorator extends AminoAcidCalculatorDecorator {

    /**
     * Constructs an {@code AthleteAminoAcidDecorator} with the given base calculator.
     *
     * @param wrapped The base {@code AminoAcidCalculator} instance to be decorated.
     */
    public AthleteAminoAcidDecorator(AminoAcidCalculator wrapped) {
        super(wrapped);
    }

    /**
     * Adjusts the daily amino acid requirements for athletes.
     * Increases BCAAs (Leucine, Isoleucine, Valine) by 30% and all other amino acids by 15%.
     *
     * @param dailyNeeds A map of baseline amino acid requirements before adjustments.
     * @return A modified map with increased amino acid needs for athletes.
     */
    @Override
    public Map<String, Double> calculateAminoAcids(Map<String, Double> dailyNeeds) {
        Map<String, Double> aminoAcids = super.calculateAminoAcids(dailyNeeds);

        // Increase BCAAs (Leucine, Isoleucine, Valine) by 30%
        String[] BCAAs = {"Leucin", "Isoleucin", "Valin"};
        for (String bcaa : BCAAs) {
            aminoAcids.computeIfPresent(bcaa, (k, v) -> v * 1.3);
        }

        // Increase all other amino acids by 15%
        aminoAcids.forEach((key, value) -> {
            if (!List.of(BCAAs).contains(key)) {
                aminoAcids.put(key, value * 1.15);
            }
        });

        return aminoAcids;
    }
}