package com.example.NutritionTracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "foodItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NutritionLogFoodItem> nutritionLogFoodItems;

    @ElementCollection(fetch = FetchType.EAGER)
    private Map<String, Double> aminoAcidProfile;

    @Version
    @Builder.Default
    private Long version = 0L;

    public FoodItem(String name, Map<String, Double> aminoAcidProfile) {
        this.name = name;
        this.aminoAcidProfile = aminoAcidProfile;
    }
}

