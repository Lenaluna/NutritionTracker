package com.example.NutritionTracker.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nutrition_log_food_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class NutritionLogFoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "nutrition_log_id", nullable = false)
    private NutritionLog nutritionLog;

    @ManyToOne
    @JoinColumn(name = "food_item_id", nullable = false)
    private FoodItem foodItem;

    public NutritionLogFoodItem(NutritionLog nutritionLog, FoodItem foodItem) {
        this.nutritionLog = nutritionLog;
        this.foodItem = foodItem;
    }
}