package com.example.NutritionTracker.repo;

import com.example.NutritionTracker.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FoodItemRepository extends JpaRepository<FoodItem, UUID> {
    Optional<FoodItem> findById(UUID id);
}
