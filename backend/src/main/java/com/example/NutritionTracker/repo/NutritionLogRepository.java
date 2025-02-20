package com.example.NutritionTracker.repo;

import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface NutritionLogRepository extends JpaRepository<NutritionLog, UUID> {

    Optional<NutritionLog> findByUserAndLogDateTime(User user, LocalDateTime logDateTime);
}