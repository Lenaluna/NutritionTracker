package com.example.NutritionTracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * Entity representing a user in the Nutrition Tracker system.
 * Each user can have multiple nutrition logs.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "app_user")
public class User {

    /** The unique identifier for the user. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** The user's name. Cannot be null. */
    @Column(nullable = false)
    private String name;

    /** The user's age. */
    private Integer age;

    /** The user's weight in kilograms. */
    private Double weight;

    /** Indicates whether the user is an athlete. */
    private Boolean isAthlete;

    /** Indicates whether the user follows a vegan diet. */
    private Boolean isVegan;

    /** Indicates whether the user is focused on longevity-based nutrition. */
    private Boolean isLongevityFocused;

    /**
     * List of nutrition logs associated with the user.
     * The {@code @JsonIgnore} annotation prevents infinite recursion during JSON serialization.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<NutritionLog> nutritionLogs;

    /**
     * Version field for optimistic locking.
     * Helps prevent conflicts when multiple transactions modify the same entity.
     */
    @Version
    private Long version;
}