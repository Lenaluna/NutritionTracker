package com.example.NutritionTracker.service;

import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.repo.FoodItemRepository;
import jakarta.persistence.EntityNotFoundException;
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

    /** Retrieves a FoodItem by ID */
    public Optional<FoodItem> getFoodItemById(UUID id) {
        return foodItemRepository.findById(id);
    }

    /** Saves a new FoodItem */
    @Transactional
    public FoodItem saveFoodItem(FoodItem foodItem) {
        if (foodItem.getName() == null || foodItem.getName().isBlank()) {
            throw new IllegalArgumentException("Name of the FoodItem cannot be null or empty");
        }
        return foodItemRepository.save(foodItem);
    }

    @Transactional
    public List<FoodItem> saveAllFoodItems(List<FoodItem> foodItems) {
        return foodItemRepository.saveAll(foodItems);
    }

    /** Updates an existing FoodItem */
    @Transactional
    public Optional<FoodItem> updateFoodItem(UUID id, FoodItem updatedFoodItem) {
        return foodItemRepository.findById(id).map(existingItem -> {
            existingItem.setName(updatedFoodItem.getName());
            existingItem.setAminoAcidProfile(updatedFoodItem.getAminoAcidProfile());
            return foodItemRepository.save(existingItem);
        });
    }
    public List<FoodItem> findByNameContainingIgnoreCase(String namePart) {
        return foodItemRepository.findByNameContainingIgnoreCase(namePart);
    }

    /** Deletes a FoodItem by ID */
    @Transactional
    public void deleteFoodItem(UUID foodItemId) {
        if (!foodItemRepository.existsById(foodItemId)) {
            throw new EntityNotFoundException("FoodItem with ID " + foodItemId + " not found");
        }
        foodItemRepository.deleteById(foodItemId);
    }}