package com.example.NutritionTracker.repo;

import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.entity.NutritionLogFoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link NutritionLogFoodItem} entities.
 * This interface extends {@link JpaRepository} to provide CRUD operations
 * for food items within a nutrition log.
 */
@Repository
public interface NutritionLogFoodItemRepository extends JpaRepository<NutritionLogFoodItem, Long> {

      /**
       * Deletes all {@link NutritionLogFoodItem} entries associated with a given {@link NutritionLog}.
       *
       * @param nutritionLog the nutrition log for which all associated food items should be deleted.
       */
      void deleteByNutritionLog(NutritionLog nutritionLog);
}