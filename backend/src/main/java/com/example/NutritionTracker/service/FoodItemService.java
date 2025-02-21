package com.example.NutritionTracker.service;

import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.repo.FoodItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;

    /** Returns all FoodItems */
    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAll();
    }

    public Optional<FoodItem> getFoodItemById(UUID id) {
        return foodItemRepository.findById(id);
    }

    /** Saves a single FoodItem */
    @Transactional
    public FoodItem saveFoodItem(FoodItem foodItem) {
        return foodItemRepository.save(foodItem);
    }

    /** Saves a list of FoodItems */
    @Transactional
    public void saveAllFoodItems(List<FoodItem> foodItems) {
        foodItemRepository.saveAll(foodItems);
    }

    /** Deletes a food item from the database */
    @Transactional
    public void deleteFoodItem(UUID foodItemId) {
        foodItemRepository.deleteById(foodItemId);
    }
}