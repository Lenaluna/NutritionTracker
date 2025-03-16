package com.example.NutritionTracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;
import java.util.UUID;

/**
 * Entity representing a food item that contains various amino acids.
 * Each food item has an amino acid profile, mapping amino acid names to their respective values.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodItem {

    /** The unique identifier for the food item. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** The name of the food item. */
    private String name;

    /**
     * A mapping of amino acid names to their respective values.
     * This represents the amino acid profile of the food item.
     */
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "food_item_amino_acids", joinColumns = @JoinColumn(name = "food_item_id"))
    @MapKeyColumn(name = "amino_acid")
    @Column(name = "amino_acid_value")
    private Map<String, Double> aminoAcidProfile;

    /**
     * Version field for optimistic locking.
     * Ensures that updates to the entity are not overwritten by concurrent transactions.
     */
    @Version
    @Builder.Default
    private Long version = 0L;
}