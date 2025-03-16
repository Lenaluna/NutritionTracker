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

import java.util.*;

/**
 * Service responsible for managing amino acid profile calculations.
 */
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

    /**
     * Calculates the sum of all consumed amino acids in the latest nutrition log.
     * @return A map containing amino acids and their summed values.
     */
    @Transactional(readOnly = true)
    public Map<String, Double> calculateAminoAcidSumsForLatestLog() {
        logger.info("Calculating amino acid sums for the latest nutrition log...");

        NutritionLog latestLog = nutritionLogRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new IllegalArgumentException("No NutritionLog found!"));

        logger.info("Found NutritionLog with ID: {}", latestLog.getId());

        if (latestLog.getFoodItems() == null || latestLog.getFoodItems().isEmpty()) {
            logger.warn("No food items found in the latest NutritionLog.");
            return Collections.emptyMap();
        }

        Map<String, Double> aminoAcidSums = new HashMap<>();
        for (NutritionLogFoodItem logFoodItem : latestLog.getFoodItems()) {
            FoodItem foodItem = logFoodItem.getFoodItem();
            logger.info("Processing FoodItem: {}", foodItem.getName());

            if (foodItem.getAminoAcidProfile() != null) {
                foodItem.getAminoAcidProfile().forEach((amino, value) -> {
                    aminoAcidSums.merge(amino, value, Double::sum);
                });
            }
        }

        logger.info("Amino acid sums calculated: {}", aminoAcidSums);
        return aminoAcidSums;
    }

    /**
     * Calculates the daily amino acid requirements based on the user's weight and profile settings.
     * @return A map of amino acids with their required daily intake values.
     */
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

    /**
     * Calculates the amino acid coverage percentage based on the latest log and daily requirements.
     * @return A map containing amino acids and their percentage coverage values.
     */
    @Transactional(readOnly = true)
    public Map<String, Double> calculateAminoAcidCoverageForLatestLog() {
        logger.info("Calculating amino acid coverage for the latest NutritionLog...");

        Map<String, Double> dailyNeeds = calculateDailyAminoAcidNeeds();
        if (dailyNeeds == null || dailyNeeds.isEmpty()) {
            logger.error("Error: No daily amino acid requirements found!");
            return Collections.emptyMap();
        }
        logger.info("Daily amino acid requirements: {}", dailyNeeds);

        Map<String, Double> consumedAminoAcids = calculateAminoAcidSumsForLatestLog();
        if (consumedAminoAcids == null || consumedAminoAcids.isEmpty()) {
            logger.warn("No consumed amino acids found. Returning empty result.");
            return Collections.emptyMap();
        }
        logger.info("Consumed amino acids: {}", consumedAminoAcids);

        Map<String, Double> coverage = new HashMap<>();
        for (String aminoAcid : dailyNeeds.keySet()) {
            double need = dailyNeeds.getOrDefault(aminoAcid, 0.0);
            double consumed = consumedAminoAcids.getOrDefault(aminoAcid, 0.0);

            if (need > 0) {
                double percentage = Math.round((consumed / need) * 100 * 100.0) / 100.0;
                coverage.put(aminoAcid, percentage);
            } else {
                logger.warn("Daily need for {} is 0. Preventing division by zero.", aminoAcid);
                coverage.put(aminoAcid, 0.0);
            }
        }

        logger.info("Amino acid coverage calculated: {}", coverage);
        return coverage;
    }
}
