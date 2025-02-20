package com.example.NutritionTracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "nutrition_log_food_items",
            joinColumns = @JoinColumn(name = "nutrition_log_id"),
            inverseJoinColumns = @JoinColumn(name = "food_item_id")
    )
    @NotEmpty
    private List<FoodItem> foodItems;

    private LocalDateTime logDateTime;}