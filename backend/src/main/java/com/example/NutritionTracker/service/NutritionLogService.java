package com.example.NutritionTracker.service;

import com.example.NutritionTracker.dto.NutritionLogCreateDTO;
import com.example.NutritionTracker.dto.NutritionLogDTO;
import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.entity.NutritionLogFoodItem;
import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.repo.FoodItemRepository;
import com.example.NutritionTracker.repo.NutritionLogFoodItemRepository;
import com.example.NutritionTracker.repo.NutritionLogRepository;
import com.example.NutritionTracker.repo.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service class responsible for managing NutritionLogs.
 */
@Service
@RequiredArgsConstructor
public class NutritionLogService {

    private static final Logger logger = LoggerFactory.getLogger(NutritionLogService.class);

    private final NutritionLogRepository nutritionLogRepository;
    private final FoodItemRepository foodItemRepository;
    private final NutritionLogFoodItemRepository nutritionLogFoodItemRepository;
    private final UserRepository userRepository;

    /**
     * Cleans up all nutrition logs before application shutdown.
     */
    @Transactional
    public void cleanup() {
        logger.info("Cleaning up database before shutdown...");
        nutritionLogRepository.deleteAll();
        logger.info("All nutrition logs deleted.");
    }

    /**
     * Adds a food item to an existing NutritionLog.
     *
     * @param logId The UUID of the NutritionLog.
     * @param foodItemId The UUID of the FoodItem to be added.
     */
    @Transactional
    public void addFoodItemToLog(UUID logId, UUID foodItemId) {
        NutritionLog nutritionLog = nutritionLogRepository.findById(logId)
                .orElseThrow(() -> new EntityNotFoundException("NutritionLog not found with ID: " + logId));

        FoodItem foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new EntityNotFoundException("FoodItem not found with ID: " + foodItemId));

        // Check if the FoodItem is already in the NutritionLog
        if (nutritionLog.getFoodItems().stream().anyMatch(item -> item.getFoodItem().getId().equals(foodItemId))) {
            logger.warn("FoodItem {} is already present in NutritionLog {}", foodItemId, logId);
            return;
        }

        NutritionLogFoodItem logFoodItem = new NutritionLogFoodItem();
        logFoodItem.setNutritionLog(nutritionLog);
        logFoodItem.setFoodItem(foodItem);

        nutritionLog.getFoodItems().add(logFoodItem);
        nutritionLogFoodItemRepository.save(logFoodItem);
        nutritionLogRepository.save(nutritionLog);

        logger.info("Added FoodItem {} to NutritionLog {}", foodItemId, logId);
    }

    /**
     * Creates a new NutritionLog and saves it to the database.
     *
     * @param log The NutritionLog entity to be created.
     * @return The saved NutritionLog.
     */
    @Transactional
    public NutritionLog createLog(NutritionLog log) {
        NutritionLog savedLog = nutritionLogRepository.save(log);

        // Ensure the log was successfully saved
        savedLog = nutritionLogRepository.findById(savedLog.getId()).orElse(null);
        if (savedLog == null || savedLog.getId() == null) {
            throw new IllegalStateException("Error saving NutritionLog.");
        }

        return savedLog;
    }

    /**
     * Retrieves a NutritionLog by its UUID.
     *
     * @param logId The UUID of the NutritionLog.
     * @return The NutritionLog entity.
     */
    @Transactional(readOnly = true)
    public NutritionLog getNutritionLogById(UUID logId) {
        if (logId == null) {
            logger.error("The provided logId is null. Please provide a valid UUID.");
            throw new IllegalArgumentException("logId cannot be null!");
        }

        logger.info("Attempting to retrieve NutritionLog with logId: {}", logId);

        return nutritionLogRepository.findById(logId)
                .map(nutritionLog -> {
                    logger.info("NutritionLog successfully retrieved: {}", nutritionLog);
                    return nutritionLog;
                })
                .orElseThrow(() -> {
                    logger.warn("NutritionLog with logId {} was not found.", logId);
                    return new IllegalArgumentException("NutritionLog with UUID " + logId + " was not found.");
                });
    }

    /**
     * Creates a new NutritionLog from frontend data.
     * If an existing log exists for the user, it is deleted before creating a new one.
     *
     * @param logDTO DTO containing the user ID.
     * @return The newly created NutritionLog.
     */
    @Transactional
    public NutritionLog createLogFromFrontend(NutritionLogCreateDTO logDTO) {
        logger.info("📝 Received request to create or update a NutritionLog: {}", logDTO);

        if (logDTO.getUserId() == null) {
            logger.error("Error: User ID is null! NutritionLog cannot be created.");
            throw new IllegalArgumentException("User ID cannot be null!");
        }

        logger.info("🔍 Searching for user with ID: {}", logDTO.getUserId());

        // Retrieve the user from the database
        User user = userRepository.findById(logDTO.getUserId())
                .orElseThrow(() -> {
                    logger.error("User with ID {} not found!", logDTO.getUserId());
                    return new EntityNotFoundException("User with ID " + logDTO.getUserId() + " not found");
                });

        logger.info("User found: {} (ID: {})", user.getName(), user.getId());

        // Check if an existing NutritionLog exists for this user
        Optional<NutritionLog> existingLog = nutritionLogRepository.findByUser(user);

        if (existingLog.isPresent()) {
            NutritionLog oldLog = existingLog.get();
            logger.warn("Existing NutritionLog with ID {} found. Deleting old log and associated entries...", oldLog.getId());

            // Remove all NutritionLogFoodItem entries related to this log
            nutritionLogFoodItemRepository.deleteByNutritionLog(oldLog);

            // Delete the old NutritionLog
            nutritionLogRepository.delete(oldLog);
            logger.info("🗑️ Old NutritionLog and its food item entries successfully deleted.");
        }

        // Create a new NutritionLog for the user
        NutritionLog newLog = NutritionLog.builder()
                .user(user)
                .build();

        // Save the new NutritionLog
        NutritionLog savedLog = nutritionLogRepository.save(newLog);

        // Ensure the log was successfully saved
        if (savedLog.getId() == null) {
            logger.error("Error saving the NutritionLog! The object did not receive an ID.");
            throw new IllegalStateException("Error saving NutritionLog.");
        }

        logger.info("Successfully created new NutritionLog with ID: {}", savedLog.getId());

        return savedLog;
    }

    /**
     * Retrieves the latest NutritionLog from the database.
     *
     * @return An Optional containing the latest NutritionLogDTO if present.
     */
    @Transactional(readOnly = true)
    public Optional<NutritionLogDTO> getLatestNutritionLog() {
        return nutritionLogRepository.findTopByOrderByIdDesc()
                .map(NutritionLogDTO::new); // Convert NutritionLog to NutritionLogDTO if present
    }

    /**
     * Retrieves the latest NutritionLog entity.
     *
     * @return An Optional containing the latest NutritionLog entity if present.
     */
    @Transactional(readOnly = true)
    public Optional<NutritionLog> getLatestNutritionLogEntity() {
        return nutritionLogRepository.findTopByOrderByIdDesc(); // Returns the entity directly
    }
}

