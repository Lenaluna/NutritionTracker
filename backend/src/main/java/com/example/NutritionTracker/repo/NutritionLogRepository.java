package com.example.NutritionTracker.repo;

import com.example.NutritionTracker.entity.NutritionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NutritionLogRepository extends JpaRepository<NutritionLog, UUID> {
}
