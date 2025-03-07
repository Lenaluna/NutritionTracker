package com.example.NutritionTracker.service;

import com.example.NutritionTracker.dto.UserDTO;
import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.entity.NutritionLogFoodItem;
import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.repo.FoodItemRepository;
import com.example.NutritionTracker.repo.NutritionLogFoodItemRepository;
import com.example.NutritionTracker.repo.NutritionLogRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class NutritionLogService {

    private static final Logger logger = LoggerFactory.getLogger(NutritionLogService.class);

    private final NutritionLogRepository nutritionLogRepository;
    private final UserService userService;
    private final AminoAcidCalculator baseCalculator = new BasicAminoAcidCalculator();
    private final FoodItemRepository foodItemRepository;
    private final NutritionLogFoodItemRepository nutritionLogFoodItemRepository;

    @Transactional(readOnly = true)
    public UserDTO getUser() {
        return userService.getUser()
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .age(user.getAge())
                        .weight(user.getWeight())
                        .isAthlete(user.getIsAthlete())
                        .build())
                .orElseGet(() -> UserDTO.builder()
                        .name("Default User")
                        .age(30)
                        .weight(70.0)
                        .isAthlete(false)
                        .build());
    }

    @Transactional
    public void cleanup() {
        logger.info("Cleaning up database before shutdown...");
        nutritionLogRepository.deleteAll();
        logger.info("All nutrition logs deleted.");
    }

    @Transactional
    public void removeFoodItemFromLog(UUID logId, UUID foodItemId) {

        if (!nutritionLogRepository.existsById(logId)) {
            throw new EntityNotFoundException("NutritionLog not found");
        }

        Optional<NutritionLogFoodItem> nutritionLogFoodItem = nutritionLogFoodItemRepository
                .findByNutritionLogIdAndFoodItemId(logId, foodItemId);

        if (nutritionLogFoodItem.isPresent()) {
            nutritionLogFoodItemRepository.delete(nutritionLogFoodItem.get());
            logger.info("Deleted FoodItem with ID {} from NutritionLog {}", foodItemId, logId);
        } else {
            logger.warn("FoodItem with ID {} not found in NutritionLog {}", foodItemId, logId);
            throw new EntityNotFoundException("FoodItem not found in this NutritionLog");
        }
    }

    @Transactional
    public void addFoodItemToLog(UUID logId, UUID foodItemId) {
        NutritionLog nutritionLog = nutritionLogRepository.findById(logId)
                .orElseThrow(() -> new EntityNotFoundException("NutritionLog not found"));

        FoodItem foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new EntityNotFoundException("FoodItem not found"));

        // Nullprüfung für foodItems-Liste
        if (nutritionLog.getFoodItems() == null) {
            nutritionLog.setFoodItems(new ArrayList<>());
            logger.warn("foodItems list was null for NutritionLog with ID: {}. Initialized an empty list.", logId);
        }

        NutritionLogFoodItem logFoodItem = new NutritionLogFoodItem();
        logFoodItem.setNutritionLog(nutritionLog);
        logFoodItem.setFoodItem(foodItem);

        nutritionLogFoodItemRepository.save(logFoodItem);

        // Hinzufügen des neuen FoodItems zur Liste
        nutritionLog.getFoodItems().add(logFoodItem);

        logger.info("Added FoodItem {} to NutritionLog {}", foodItemId, logId);
    }

    @Transactional(readOnly = true)
    public List<NutritionLog> getAllLogs() {
        logger.info("Fetching all nutrition logs. Total logs: {}", nutritionLogRepository.count());
        return nutritionLogRepository.findAll();
    }


    @Transactional(readOnly = true)
    public Optional<NutritionLog> getLogById(UUID id) {
        Optional<NutritionLog> nutritionLog = nutritionLogRepository.findById(id);

        nutritionLog.ifPresent(log -> Hibernate.initialize(log.getFoodItems()));

        return nutritionLog;
    }

    @Transactional
    public NutritionLog createLog(NutritionLog log) {
        UUID userId = log.getUser().getId();
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new EntityNotFoundException("User mit ID: " + userId + " wurde nicht gefunden.");
        }

        log.setUser(user);

        return nutritionLogRepository.save(log);
    }

    @Transactional
    public void deleteLog(UUID id) {
        if (nutritionLogRepository.existsById(id)) {
            nutritionLogRepository.deleteById(id);
            logger.info("Deleted NutritionLog with ID: {}", id);
        } else {
            logger.warn("NutritionLog with ID {} not found for deletion", id);
        }
    }

    @Transactional
    public NutritionLog updateLog(UUID id, NutritionLog updatedLog) {
        return nutritionLogRepository.findById(id).map(existingLog -> {
            existingLog.setFoodItems(updatedLog.getFoodItems());
            existingLog.setLogDateTime(updatedLog.getLogDateTime());
            NutritionLog savedLog = nutritionLogRepository.save(existingLog);
            logger.info("Updated NutritionLog with ID: {}", id);
            return savedLog;
        }).orElseThrow(() -> {
            logger.error("Attempted to update non-existent NutritionLog with ID: {}", id);
            return new EntityNotFoundException("Log not found");
        });
    }

    @Transactional(readOnly = true)
    public Map<String, Double> calculateAminoAcidsForLog(UUID logId) {
        NutritionLog log = nutritionLogRepository.findById(logId)
                .orElseThrow(() -> new EntityNotFoundException("NutritionLog not found"));

        UserDTO userDTO = userService.getUser()
                .orElseThrow(() -> new EntityNotFoundException("No user found"));

        AminoAcidCalculator calculator = baseCalculator;

        if (userDTO.getAge() < 18) {
            calculator = new ChildAminoAcidDecorator(calculator);
            logger.info("Applying ChildAminoAcidDecorator for User: {}", userDTO.getName());
        }

        if (Boolean.TRUE.equals(userDTO.getIsAthlete())) {
            calculator = new AthleteAminoAcidDecorator(calculator);
            logger.info("Applying AthleteAminoAcidDecorator for User: {}", userDTO.getName());
        }

        logger.info("Calculating amino acids for NutritionLog with ID: {}", log.getId());
        return calculator.calculateAminoAcids(log);
    }
}