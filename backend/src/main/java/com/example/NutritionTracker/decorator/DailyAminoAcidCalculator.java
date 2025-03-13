package com.example.NutritionTracker.decorator;

import com.example.NutritionTracker.dto.UserDTO;
import com.example.NutritionTracker.entity.AminoAcidRequirement;
import com.example.NutritionTracker.repo.AminoAcidRequirementRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DailyAminoAcidCalculator implements AminoAcidCalculator {
    private final AminoAcidRequirementRepository aminoAcidRequirementRepository;

    public DailyAminoAcidCalculator(AminoAcidRequirementRepository repository) {
        this.aminoAcidRequirementRepository = repository;
    }

    @Override
    public Map<String, Double> calculateAminoAcids(Map<String, Double> dailyNeeds) {
        return dailyNeeds;
    }

    /**
     * Berechnet den individuellen Tagesbedarf basierend auf Gewicht.
     */
    public Map<String, Double> calculateDailyNeeds(UserDTO user) {
        List<AminoAcidRequirement> requirements = aminoAcidRequirementRepository.findAll();
        Map<String, Double> dailyNeeds = new HashMap<>();

        for (AminoAcidRequirement req : requirements) {
            double need = req.getBaseAmountPerKg() * user.getWeight();
            dailyNeeds.merge(req.getAminoAcid(), need, Double::sum);
        }

        return dailyNeeds;
    }
}
