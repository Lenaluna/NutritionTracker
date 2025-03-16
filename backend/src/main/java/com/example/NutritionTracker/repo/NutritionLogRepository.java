package com.example.NutritionTracker.repo;

import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing {@link NutritionLog} entities.
 * Provides database operations for retrieving and managing nutrition logs.
 */
@Repository
public interface NutritionLogRepository extends JpaRepository<NutritionLog, UUID> {

    /**
     * Retrieves the most recent {@link NutritionLog} entry based on ID order.
     *
     * @return an {@link Optional} containing the most recently created nutrition log, if available.
     */
    Optional<NutritionLog> findTopByOrderByIdDesc();

    /**
     * Retrieves a {@link NutritionLog} associated with a specific {@link User}.
     *
     * @param user the user whose nutrition log is being queried.
     * @return an {@link Optional} containing the user's nutrition log, if found.
     */
    Optional<NutritionLog> findByUser(User user);
}