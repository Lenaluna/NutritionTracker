package com.example.NutritionTracker.entity;

import jakarta.persistence.*;
import java.util.Map;
import java.util.UUID;

@Entity
public class FoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Double proteinContent;

    @ElementCollection
    @CollectionTable(name = "amino_acid_profile", joinColumns = @JoinColumn(name = "food_item_id"))
    @MapKeyColumn(name = "amino_acid")
    @Column(name = "value")
    private Map<String, Double> aminoAcidProfile;

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getProteinContent() {
        return proteinContent;
    }

    public void setProteinContent(Double proteinContent) {
        this.proteinContent = proteinContent;
    }

    public Map<String, Double> getAminoAcidProfile() {
        return aminoAcidProfile;
    }

    public void setAminoAcidProfile(Map<String, Double> aminoAcidProfile) {
        this.aminoAcidProfile = aminoAcidProfile;
    }
}
