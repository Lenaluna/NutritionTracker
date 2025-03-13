package com.example.NutritionTracker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class NutritionLogFoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "nutrition_log_id", nullable = false)
    @JsonBackReference
    private NutritionLog nutritionLog;

    @ManyToOne
    @JoinColumn(name = "food_item_id", nullable = false)
    private FoodItem foodItem;

    public NutritionLogFoodItem(NutritionLog nutritionLog, FoodItem foodItem) {
        this.nutritionLog = nutritionLog;
        this.foodItem = foodItem;
    }



    @Version
    private Long version;
}