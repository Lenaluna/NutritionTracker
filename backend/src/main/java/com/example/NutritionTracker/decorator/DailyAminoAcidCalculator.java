package com.example.NutritionTracker.decorator;

import com.example.NutritionTracker.dto.UserDTO;
import com.example.NutritionTracker.entity.AminoAcidRequirement;
import com.example.NutritionTracker.repo.AminoAcidRequirementRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service responsible for calculating daily amino acid requirements.
 * Implements {@link AminoAcidCalculator} to provide baseline amino acid calculations.
 */
@Service
public class DailyAminoAcidCalculator implements AminoAcidCalculator {

    private final AminoAcidRequirementRepository aminoAcidRequirementRepository;

    /**
     * Constructs a {@code DailyAminoAcidCalculator} with a reference to the amino acid requirement repository.
     *
     * @param repository The repository providing base amino acid requirements per kilogram.
     */
    public DailyAminoAcidCalculator(AminoAcidRequirementRepository repository) {
        this.aminoAcidRequirementRepository = repository;
    }

    /**
     * Returns the input daily needs without modifications.
     * Used when no specific decorator is applied.
     *
     * @param dailyNeeds A map of baseline daily amino acid requirements.
     * @return The unchanged daily amino acid requirements.
     */
    @Override
    public Map<String, Double> calculateAminoAcids(Map<String, Double> dailyNeeds) {
        return dailyNeeds;
    }

    /**
     * Calculates the individual daily amino acid needs based on the user's weight.
     * Converts the amino acid requirement from mg/kg to g/day.
     *
     * @param user The user whose amino acid requirements are to be calculated.
     * @return A map containing the calculated daily amino acid needs.
     */
    public Map<String, Double> calculateDailyNeeds(UserDTO user) {
        List<AminoAcidRequirement> requirements = aminoAcidRequirementRepository.findAll();
        Map<String, Double> dailyNeeds = new HashMap<>();

        for (AminoAcidRequirement req : requirements) {
            double need = (req.getBaseAmountPerKg() * user.getWeight()) / 1000; // Convert mg to g
            dailyNeeds.merge(req.getAminoAcid(), need, Double::sum);
        }

        return dailyNeeds;
    }
}