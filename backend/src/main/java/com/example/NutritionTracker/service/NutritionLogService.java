package com.example.NutritionTracker.service;

import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.repo.FoodItemRepository;
import com.example.NutritionTracker.repo.NutritionLogRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NutritionLogService {

    private final NutritionLogRepository nutritionLogRepository;
    private final AminoAcidCalculator baseCalculator = new BasicAminoAcidCalculator();
    private final FoodItemRepository foodItemRepository;

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

    public NutritionLog updateLog(UUID id, NutritionLog updatedLog) {
        return nutritionLogRepository.findById(id)
                .map(existingLog -> {
                    existingLog.setFoodItems(updatedLog.getFoodItems());
                    existingLog.setTotalProtein(updatedLog.getTotalProtein());
                    existingLog.setLogDate(updatedLog.getLogDate());
                    return nutritionLogRepository.save(existingLog);
                }).orElseThrow(() -> new RuntimeException("Log not found"));
    }

    public Map<String, Double> calculateAminoAcidsForLog(NutritionLog log) {
        AminoAcidCalculator calculator = baseCalculator;

        if (log.getUser() != null) {
            if (log.getUser().getAge() < 18) {
                calculator = new ChildAminoAcidDecorator(calculator);
            }

            if (log.getUser().getIsAthlete()) {
                calculator = new AthleteAminoAcidDecorator(calculator);
            }
        }
        return calculator.calculateAminoAcids(log);
    }
    public FoodItem updateFoodItem(UUID id, FoodItem updatedItem) {
        return foodItemRepository.findById(id)
                .map(existingItem -> {
                    existingItem.setName(updatedItem.getName());
                    existingItem.setProteinContent(updatedItem.getProteinContent());
                    existingItem.setAminoAcidProfile(updatedItem.getAminoAcidProfile());
                    return foodItemRepository.save(existingItem);
                }).orElseThrow(() -> new RuntimeException("FoodItem not found"));
    }
}



