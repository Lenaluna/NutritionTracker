package com.example.NutritionTracker.repo;

import com.example.NutritionTracker.entity.NutritionLogFoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NutritionLogFoodItemRepository extends JpaRepository<NutritionLogFoodItem, Long> {
    Optional<NutritionLogFoodItem> findByNutritionLogIdAndFoodItemId(UUID nutritionLogId, UUID foodItemId);
}