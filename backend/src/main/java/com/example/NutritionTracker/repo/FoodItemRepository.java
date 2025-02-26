package com.example.NutritionTracker.repo;

import com.example.NutritionTracker.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, UUID> {
    List<FoodItem> findByNameContainingIgnoreCase(String name);
}
