package com.example.NutritionTracker.repo;

import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NutritionLogRepository extends JpaRepository<NutritionLog, UUID> {
    Optional<NutritionLog> findTopByOrderByIdDesc();
    Optional<NutritionLog> findByUser(User user);
}