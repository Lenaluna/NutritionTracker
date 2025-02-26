package com.example.NutritionTracker.service;

import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.entity.NutritionLogFoodItem;
import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.repo.FoodItemRepository;
import com.example.NutritionTracker.repo.NutritionLogFoodItemRepository;
import com.example.NutritionTracker.repo.NutritionLogRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NutritionLogService {

    private static final Logger logger = LoggerFactory.getLogger(NutritionLogService.class);

    private final NutritionLogRepository nutritionLogRepository;
    private final UserService userService;
    private final AminoAcidCalculator baseCalculator = new BasicAminoAcidCalculator();
    private final FoodItemRepository foodItemRepository;
    private final NutritionLogFoodItemRepository nutritionLogFoodItemRepository;


    public User getUser() {
        return userService.getUser()
                .orElseGet(() -> User.builder()
                        .name("Default User")
                        .age(30)
                        .weight(70.0)
                        .isAthlete(false)
                        .build());
    }

    @Transactional
    public void removeFoodItemFromLog(UUID logId, UUID foodItemId) {
        NutritionLog log = nutritionLogRepository.findById(logId)
                .orElseThrow(() -> new EntityNotFoundException("NutritionLog not found"));

        Optional<NutritionLogFoodItem> nutritionLogFoodItem = nutritionLogFoodItemRepository
                .findByNutritionLogIdAndFoodItemId(logId, foodItemId);

        if (nutritionLogFoodItem.isPresent()) {
            nutritionLogFoodItemRepository.delete(nutritionLogFoodItem.get()); // Löscht direkt aus der DB
            logger.info("Deleted FoodItem with ID {} from NutritionLog {}", foodItemId, logId);
        } else {
            logger.warn("FoodItem with ID {} not found in NutritionLog {}", foodItemId, logId);
            throw new EntityNotFoundException("FoodItem not found in this NutritionLog");
        }
    }
    @Transactional
    public void addFoodItemToLog(UUID logId,  UUID foodItemId) {
        NutritionLog nutritionLog = nutritionLogRepository.findById(logId)
                .orElseThrow(() -> new EntityNotFoundException("NutritionLog not found"));

        FoodItem foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new EntityNotFoundException("FoodItem not found"));

        NutritionLogFoodItem nutritionLogFoodItem = new NutritionLogFoodItem(nutritionLog, foodItem);

        nutritionLog.getFoodItems().add(nutritionLogFoodItem);
        nutritionLogFoodItemRepository.save(nutritionLogFoodItem);
    }

    /**
     * Returns all NutritionLogs stored in the H2 database.
     */
    public List<NutritionLog> getAllLogs() {
        logger.info("Fetching all nutrition logs. Total logs: {}", nutritionLogRepository.count());
        return nutritionLogRepository.findAll();
    }

    /**
     * Finds a NutritionLog by its ID from the H2 database.
     */
    public Optional<NutritionLog> getLogById(UUID id) {
        logger.info("Searching for NutritionLog with ID: {}", id);
        return nutritionLogRepository.findById(id);
    }

    /**
     * Creates a new NutritionLog and saves it to the H2 database.
     */
    public NutritionLog createLog(NutritionLog log) {
        NutritionLog savedLog = nutritionLogRepository.save(log);
        logger.info("New NutritionLog with ID {} created.", savedLog.getId());
        return savedLog;
    }

    /**
     * Deletes a NutritionLog by its ID from the H2 database.
     */
    public void deleteLog(UUID id) {
        if (nutritionLogRepository.existsById(id)) {
            nutritionLogRepository.deleteById(id);
            logger.info("Deleted NutritionLog with ID: {}", id);
        } else {
            logger.warn("NutritionLog with ID {} not found for deletion", id);
        }
    }

    /**
     * Updates an existing NutritionLog in the H2 database.
     */
    public NutritionLog updateLog(UUID id, NutritionLog updatedLog) {
        return nutritionLogRepository.findById(id).map(existingLog -> {
            existingLog.setFoodItems(updatedLog.getFoodItems());
            existingLog.setLogDateTime(updatedLog.getLogDateTime());
            NutritionLog savedLog = nutritionLogRepository.save(existingLog);
            logger.info("Updated NutritionLog with ID: {}", id);
            return savedLog;
        }).orElseThrow(() -> {
            logger.error("Attempted to update non-existent NutritionLog with ID: {}", id);
            return new RuntimeException("Log not found");
        });
    }

    /**
     * Calculates amino acid values based on NutritionLog data.
     * Applies additional decorators for children or athletes if applicable.
     */
    public Map<String, Double> calculateAminoAcidsForLog(NutritionLog log) {
        AminoAcidCalculator calculator = baseCalculator;

        if (log.getUser() != null) {
            if (log.getUser().getAge() < 18) {
                calculator = new ChildAminoAcidDecorator(calculator);
                logger.info("Applying ChildAminoAcidDecorator for User: {}", log.getUser().getName());
            }

            if (Boolean.TRUE.equals(log.getUser().getIsAthlete())) {
                calculator = new AthleteAminoAcidDecorator(calculator);
                logger.info("Applying AthleteAminoAcidDecorator for User: {}", log.getUser().getName());
            }
        }

        logger.info("Calculating amino acids for NutritionLog with ID: {}", log.getId());
        return calculator.calculateAminoAcids(log);
    }
}