package com.example.NutritionTracker.service;

import com.example.NutritionTracker.entity.NutritionLog;
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
}



