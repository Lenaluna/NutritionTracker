package com.example.NutritionTracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
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
    @Column(name = "amino_acid_value")
    private Map<String, Double> aminoAcidProfile;
}
