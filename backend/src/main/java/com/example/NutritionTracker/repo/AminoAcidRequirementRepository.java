package com.example.NutritionTracker.repo;

import com.example.NutritionTracker.entity.AminoAcidRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AminoAcidRequirementRepository extends JpaRepository<AminoAcidRequirement, UUID> {
    @NonNull
    List<AminoAcidRequirement> findAll();
    Optional<AminoAcidRequirement> findByAminoAcid(String aminoAcid);
}