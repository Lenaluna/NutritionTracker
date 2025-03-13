package com.example.NutritionTracker.service;

import com.example.NutritionTracker.decorator.*;
import com.example.NutritionTracker.dto.UserDTO;
import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.entity.NutritionLogFoodItem;
import com.example.NutritionTracker.repo.NutritionLogFoodItemRepository;
import com.example.NutritionTracker.repo.FoodItemRepository;
import com.example.NutritionTracker.repo.NutritionLogRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AminoProfileService {
    private final FoodItemRepository foodItemRepository;
    private final NutritionLogFoodItemRepository nutritionLogFoodItemRepository;
    private final UserService userService;
    private final DailyAminoAcidCalculator dailyAminoAcidCalculator;
    private final Logger logger = LoggerFactory.getLogger(AminoProfileService.class);
    private final NutritionLogRepository nutritionLogRepository;
    private final UserDataService userDataService;

    @Transactional(readOnly = true)
    public Map<String, Double> calculateAminoAcidSumsForLatestLog() {
        // Neuestes NutritionLog abrufen
        NutritionLog latestLog = nutritionLogRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new IllegalArgumentException("Kein NutritionLog gefunden!"));

        // Summen der konsumierten Aminosäuren berechnen
        Map<String, Double> aminoAcidSums = new HashMap<>();
        for (NutritionLogFoodItem logFoodItem : latestLog.getFoodItems()) {
            FoodItem foodItem = logFoodItem.getFoodItem();
            if (foodItem.getAminoAcidProfile() != null) {
                foodItem.getAminoAcidProfile().forEach((amino, value) -> {
                    aminoAcidSums.merge(amino, value, Double::sum);
                });
            }
        }

        // Ergebnis loggen
        logger.info("Summierte Aminosäuren für das neueste NutritionLog: {}", aminoAcidSums);

        return aminoAcidSums;
    }


    @Transactional(readOnly = true)
    public Map<String, Double> calculateDailyAminoAcidNeeds() {
        UserDTO userDTO = userDataService.getUser()
                .orElseThrow(() -> new EntityNotFoundException("No user found"));

        Map<String, Double> dailyNeeds = dailyAminoAcidCalculator.calculateDailyNeeds(userDTO);
        logger.info("Base daily amino acid needs for {}: {}", userDTO.getName(), dailyNeeds);

        AminoAcidCalculator calculator = dailyAminoAcidCalculator;

        if (Boolean.TRUE.equals(userDTO.getIsAthlete())) {
            calculator = new AthleteAminoAcidDecorator(calculator);
            logger.info("Applying AthleteAminoAcidDecorator for user: {}", userDTO.getName());
        }

        if (Boolean.TRUE.equals(userDTO.getIsVegan())) {
            calculator = new VeganAminoAcidDecorator(calculator);
            logger.info("Applying VeganAminoAcidDecorator for user: {}", userDTO.getName());
        }

        if (Boolean.TRUE.equals(userDTO.getIsLongevityFocused())) {
            calculator = new LongevityAminoAcidDecorator(calculator);
            logger.info("Applying LongevityAminoAcidDecorator for user: {}", userDTO.getName());
        }

        Map<String, Double> adjustedNeeds = calculator.calculateAminoAcids(dailyNeeds);
        logger.info("Final daily amino acid needs for {}: {}", userDTO.getName(), adjustedNeeds);
        return adjustedNeeds;
    }


    @Transactional(readOnly = true)
    public Map<String, Double> calculateAminoAcidCoverageForLatestLog() {
        logger.info("Berechnung der Aminosäurenabdeckung für das neueste NutritionLog gestartet...");

        // 1. Tagesbedarf der Aminosäuren abrufen
        Map<String, Double> dailyNeeds = calculateDailyAminoAcidNeeds();
        logger.info("Tagesbedarf der Aminosäuren: {}", dailyNeeds);

        // 2. Konsumierte Aminosäuren summieren (vom neuesten Log)
        Map<String, Double> consumedAminoAcids = calculateAminoAcidSumsForLatestLog();
        logger.info("Konsumierte Aminosäuren (Summen): {}", consumedAminoAcids);

        // 3. Coverage berechnen
        Map<String, Double> coverage = new HashMap<>();
        for (String aminoAcid : dailyNeeds.keySet()) {
            // Hole Tagesbedarf (Default 1.0 für Sicherheit)
            double need = dailyNeeds.getOrDefault(aminoAcid, 1.0);

            // Hole konsumierte Menge (Default 0.0, falls nicht vorhanden)
            double consumed = consumedAminoAcids.getOrDefault(aminoAcid, 0.0);

            // Coverage berechnen: (konsumiert / Bedarf) * 100
            double percentage = (consumed / need) * 100;
            coverage.put(aminoAcid, percentage);
        }

        logger.info("Aminosäurenabdeckung berechnet: {}", coverage);

        return coverage;
    }
}
