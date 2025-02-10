package com.example.NutritionTracker.service;

import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.repo.FoodItemRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;

    public List<FoodItem> getAllFoodItems() {
        return foodItemRepository.findAll();
    }

    public Optional<FoodItem> getFoodItemById(UUID id) {
        return foodItemRepository.findById(id);
    }

    public FoodItem createFoodItem(FoodItem foodItem) {
        return foodItemRepository.save(foodItem);
    }

    public void deleteFoodItem(UUID id) {
        foodItemRepository.deleteById(id);
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
