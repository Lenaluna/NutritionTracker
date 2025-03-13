package com.example.NutritionTracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "food_item_amino_acids", joinColumns = @JoinColumn(name = "food_item_id"))
    @MapKeyColumn(name = "amino_acid")
    @Column(name = "amino_acid_value")
    private Map<String, Double> aminoAcidProfile;

    @Version
    @Builder.Default
    private Long version = 0L;
}

