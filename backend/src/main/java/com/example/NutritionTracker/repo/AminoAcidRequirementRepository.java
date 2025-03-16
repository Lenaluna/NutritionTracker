package com.example.NutritionTracker.repo;

import com.example.NutritionTracker.entity.AminoAcidRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for managing {@link AminoAcidRequirement} entities.
 * This interface extends {@link JpaRepository} to provide CRUD operations
 * for amino acid requirement data.
 */
public interface AminoAcidRequirementRepository extends JpaRepository<AminoAcidRequirement, UUID> {

    /**
     * Retrieves all stored amino acid requirements from the database.
     *
     * @return a list of all {@link AminoAcidRequirement} entities.
     */
    @NonNull
    List<AminoAcidRequirement> findAll();
}