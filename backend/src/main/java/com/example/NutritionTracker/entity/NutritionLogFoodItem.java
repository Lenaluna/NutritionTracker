package com.example.NutritionTracker.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Entity representing the relationship between a NutritionLog and a FoodItem.
 * This class links food items to a specific nutrition log entry.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class NutritionLogFoodItem {

    /** The unique identifier for this NutritionLogFoodItem entry. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * The nutrition log associated with this food item.
     * The {@code @JsonBackReference} annotation prevents infinite recursion during JSON serialization.
     */
    @ManyToOne
    @JoinColumn(name = "nutrition_log_id", nullable = false)
    @JsonBackReference
    private NutritionLog nutritionLog;

    /** The food item linked to this nutrition log entry. */
    @ManyToOne
    @JoinColumn(name = "food_item_id", nullable = false)
    private FoodItem foodItem;

    /**
     * Constructor to create a new NutritionLogFoodItem entry.
     *
     * @param nutritionLog The associated nutrition log.
     * @param foodItem     The food item being added to the log.
     */
    public NutritionLogFoodItem(NutritionLog nutritionLog, FoodItem foodItem) {
        this.nutritionLog = nutritionLog;
        this.foodItem = foodItem;
    }

    /**
     * Version field for optimistic locking.
     * Helps prevent conflicts when multiple transactions modify the same entity.
     */
    @Version
    private Long version;
}