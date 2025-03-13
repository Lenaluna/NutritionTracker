package com.example.NutritionTracker.decorator;

import java.util.List;
import java.util.Map;

public class AthleteAminoAcidDecorator extends AminoAcidCalculatorDecorator {
    public AthleteAminoAcidDecorator(AminoAcidCalculator wrapped) {
        super(wrapped);
    }

    @Override
    public Map<String, Double> calculateAminoAcids(Map<String, Double> dailyNeeds) {
        Map<String, Double> aminoAcids = super.calculateAminoAcids(dailyNeeds);

        // Spezifische Erhöhung für BCAAs um 30%
        String[] BCAAs = {"Leucin", "Isoleucin", "Valin"};
        for (String bcaa : BCAAs) {
            aminoAcids.computeIfPresent(bcaa, (k, v) -> v * 1.3);
        }

        // Alle anderen Aminosäuren um 15% erhöhen
        aminoAcids.forEach((key, value) -> {
            if (!List.of(BCAAs).contains(key)) {
                aminoAcids.put(key, value * 1.15);
            }
        });

        return aminoAcids;
    }
}