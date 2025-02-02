package com.example.NutritionTracker.service;

import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.repo.NutritionLogRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;


import java.util.*;

@Service
@RequiredArgsConstructor

public class NutritionLogService {

    private final NutritionLogRepository nutritionLogRepository;

    public List<NutritionLog> getAllLogs() {
        return nutritionLogRepository.findAll();
    }

    public Optional<NutritionLog> getLogById(UUID id) {
        return nutritionLogRepository.findById(id);
    }

    public NutritionLog createLog(NutritionLog log) {
        return nutritionLogRepository.save(log);
    }

    public void deleteLog(UUID id) {
        nutritionLogRepository.deleteById(id);
    }

    public Map<String, Object> analyzeLog(NutritionLog log) {
        Map<String, Object> analysis = new HashMap<>();

        double totalProtein = log.getFoodItems().stream()
                .mapToDouble(FoodItem::getProteinContent)
                .sum();
        analysis.put("totalProtein", totalProtein);

        Map<String, Double> totalAminoAcids = new HashMap<>();
        log.getFoodItems().forEach(item -> item.getAminoAcidProfile().forEach((aminoAcid, value) ->
                totalAminoAcids.merge(aminoAcid, value, Double::sum)
        ));
        analysis.put("aminoAcidProfile", totalAminoAcids);

        return analysis;
    }
    public Map<String, Double> calculateDailyAminoAcidNeeds(User user) {
        double weightFactor = user.getWeight() / 70.0;
        double ageFactor = user.getAge() > 50 ? 1.2 : 1.0;
        double athleteFactor = user.getIsAthlete() ? 1.5 : 1.0;

        return Map.of(
                "Lysin", 3.0 * weightFactor * ageFactor * athleteFactor,
                "Valin", 2.5 * weightFactor * ageFactor * athleteFactor,
                "Isoleucin", 2.0 * weightFactor * ageFactor * athleteFactor
        );
    }

    public void compareAminoAcidIntakeWithNeeds(String userName, Map<String, Double> logAminoAcids, Map<String, Double> dailyNeeds) {
        System.out.println("Vergleiche Aminosäurenaufnahme für Benutzer: " + userName);
        dailyNeeds.forEach((aminoAcid, neededAmount) -> {
            double intake = logAminoAcids.getOrDefault(aminoAcid, 0.0);
            if (intake >= neededAmount) {
                System.out.println(aminoAcid + ": Bedarf gedeckt (" + intake + " von " + neededAmount + " g)");
            } else {
                System.out.println(aminoAcid + ": Bedarf NICHT gedeckt (" + intake + " von " + neededAmount + " g)");
            }
        });
    }
}


