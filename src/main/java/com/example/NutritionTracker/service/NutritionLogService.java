package com.example.NutritionTracker.service;

import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.repo.NutritionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NutritionLogService {

    @Autowired
    private NutritionLogRepository nutritionLogRepository;

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

    public double calculateTotalProtein(NutritionLog log) {
        return log.getFoodItems().stream()
                .mapToDouble(item -> item.getProteinContent())
                .sum();
    }
}
