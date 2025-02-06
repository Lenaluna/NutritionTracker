package com.example.NutritionTracker.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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
    private User user;

    @ManyToMany
    private List<FoodItem> foodItems;

    private LocalDate logDate;
    private Double totalProtein;

    @ManyToOne
    private FoodItem selectedFood;
}