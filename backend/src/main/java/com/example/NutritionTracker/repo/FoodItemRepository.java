package com.example.NutritionTracker.repo;

import com.example.NutritionTracker.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing {@link FoodItem} entities.
 * This interface extends {@link JpaRepository} to provide CRUD operations
 * for food items in the database.
 */
@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, UUID> {

    /**
     * Retrieves a list of {@link FoodItem} entities by their IDs.
     *
     * @param ids a collection of UUIDs representing the food items to retrieve.
     * @return a list of matching {@link FoodItem} entities.
     */
    @NonNull
    List<FoodItem> findAllById(@NonNull Iterable<UUID> ids);
}