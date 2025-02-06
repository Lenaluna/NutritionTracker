package com.example.NutritionTracker.repo;

import com.example.NutritionTracker.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FoodItemRepository extends JpaRepository<FoodItem, UUID> {
}
