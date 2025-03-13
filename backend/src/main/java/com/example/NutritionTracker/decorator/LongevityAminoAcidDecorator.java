package com.example.NutritionTracker.decorator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class LongevityAminoAcidDecorator extends AminoAcidCalculatorDecorator {

    private static final Logger logger = LoggerFactory.getLogger(LongevityAminoAcidDecorator.class);

    public LongevityAminoAcidDecorator(AminoAcidCalculator wrapped) {
        super(wrapped);
    }

    @Override
    public Map<String, Double> calculateAminoAcids(Map<String, Double> dailyNeeds) {
        if (dailyNeeds == null) {
            throw new IllegalArgumentException("Daily needs map cannot be null");
        }

        // Basisberechnung von Aminosäuren abrufen
        Map<String, Double> aminoAcids = super.calculateAminoAcids(dailyNeeds);

        if (aminoAcids == null) {
            throw new IllegalStateException("calculateAminoAcids() from wrapped returned null");
        }

        // Sicherstellen, dass alle essenziellen Aminosäuren in der Map vorhanden sind
        String[] essentialAminoAcids = {
                "Lysin", "Phenylalanin", "Threonin", "Tryptophan",
                "Methionin", "Glycin", "Leucin", "Isoleucin", "Valin"
        };

        for (String aminoAcid : essentialAminoAcids) {
            aminoAcids.putIfAbsent(aminoAcid, 0.0);
        }

        // Anpassungsregeln für spezifische Aminosäuren
        aminoAcids.computeIfPresent("Methionin", (k, v) -> v * 0.8); // Reduktion um 20%
        aminoAcids.computeIfPresent("Glycin", (k, v) -> v * 1.25); // Erhöhung um 25%
        aminoAcids.computeIfPresent("Leucin", (k, v) -> v * 0.9); // Reduktion um 10%
        aminoAcids.computeIfPresent("Isoleucin", (k, v) -> v * 0.9); // Reduktion um 10%
        aminoAcids.computeIfPresent("Valin", (k, v) -> v * 0.9); // Reduktion um 10%
        aminoAcids.computeIfPresent("Phenylalanin", (k, v) -> v * 1.2); // Erhöhung um 20%
        aminoAcids.computeIfPresent("Threonin", (k, v) -> v * 0.95); // Reduktion um 5%
        aminoAcids.computeIfPresent("Tryptophan", (k, v) -> v * 1.1); // Erhöhung um 10%
        aminoAcids.computeIfPresent("Lysin", (k, v) -> v * 1.05); // Erhöhung um 5%

        // Logging für Debugging
        logger.info("Berechnete Aminosäuren für Longevity-Decorator: {}", aminoAcids);

        // Rückgabe der berechneten Aminosäurewerte
        return aminoAcids;
    }
}