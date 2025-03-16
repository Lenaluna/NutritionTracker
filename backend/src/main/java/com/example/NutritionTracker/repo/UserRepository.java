package com.example.NutritionTracker.repo;

import com.example.NutritionTracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for managing {@link User} entities.
 * This interface provides CRUD operations for user data using Spring Data JPA.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
}