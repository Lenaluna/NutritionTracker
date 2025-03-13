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


@Service
@RequiredArgsConstructor
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;
    private static final Logger logger = LoggerFactory.getLogger(FoodItemService.class);

    /**
     * Returns all FoodItems as DTOs.
     * @return List of FoodItemDTOs
     */
    @Transactional(readOnly = true)
    public List<FoodItemDTO> getAllFoodItems() {
        return foodItemRepository.findAll().stream()
                .map(foodItem -> new FoodItemDTO(foodItem.getId(), foodItem.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Cleans up food items from the database before shutdown.
     */
    @PreDestroy
    public void cleanup() {
        logger.info("Cleaning up food items from database...");
        foodItemRepository.deleteAll();
        logger.info("All food items deleted.");
    }

    /**
     * Saves a list of FoodItem entities to the database.
     * @param foodItems The list of FoodItem entities to save
     */
    @Transactional
    public List<FoodItem> saveAllFoodItems(List<FoodItem> foodItems) {
        logger.info("Speichere FoodItems: {}", foodItems); // Logging vor dem Speichern
        List<FoodItem> savedItems = foodItemRepository.saveAll(foodItems);
        logger.info("Gespeicherte FoodItems mit IDs: {}", savedItems.stream().map(FoodItem::getId).toList()); // Logging nach dem Speichern
        return savedItems;
    }
}