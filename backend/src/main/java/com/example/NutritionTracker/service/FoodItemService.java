package com.example.NutritionTracker.service;

import com.example.NutritionTracker.dto.FoodItemDTO;
import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.repo.FoodItemRepository;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class responsible for managing FoodItem entities.
 */
@Service
@RequiredArgsConstructor
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;
    private static final Logger logger = LoggerFactory.getLogger(FoodItemService.class);

    /**
     * Retrieves all available FoodItems as DTOs.
     *
     * @return List of FoodItemDTOs representing all food items in the database.
     */
    @Transactional(readOnly = true)
    public List<FoodItemDTO> getAllFoodItems() {
        return foodItemRepository.findAll().stream()
                .map(foodItem -> new FoodItemDTO(foodItem.getId(), foodItem.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Cleans up all food items from the database before the application shuts down.
     * This method ensures that food items are deleted when the application is stopped.
     */
    @PreDestroy
    public void cleanup() {
        logger.info("Cleaning up food items from the database...");
        foodItemRepository.deleteAll();
        logger.info("All food items have been deleted.");
    }

    /**
     * Saves a list of FoodItem entities to the database.
     *
     * @param foodItems The list of FoodItem entities to be saved.
     * @return List of saved FoodItem entities with their assigned IDs.
     */
    @Transactional
    public List<FoodItem> saveAllFoodItems(List<FoodItem> foodItems) {
        logger.info("Saving food items: {}", foodItems); // Logging before saving
        List<FoodItem> savedItems = foodItemRepository.saveAll(foodItems);
        logger.info("Saved food items with IDs: {}", savedItems.stream().map(FoodItem::getId).toList()); // Logging after saving
        return savedItems;
    }
}