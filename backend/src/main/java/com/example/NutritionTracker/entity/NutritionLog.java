package com.example.NutritionTracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entity representing a nutrition log for a user.
 * This log keeps track of food items consumed by the user.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutritionLog {

    /** The unique identifier for the nutrition log. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** The user associated with this nutrition log. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    /**
     * A list of food items associated with this nutrition log.
     * The cascade and orphan removal settings ensure that related food items
     * are properly managed when updating or deleting the log.
     */
    @OneToMany(mappedBy = "nutritionLog", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<NutritionLogFoodItem> foodItems = new ArrayList<>();

    /**
     * Version field for optimistic locking.
     * Helps prevent conflicts when multiple transactions modify the same entity.
     */
    @Version
    private Long version;
}