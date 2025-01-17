package com.example.NutritionTracker.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
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
    private List<FoodItem> foodItems;

    @Column(nullable = false)
    private LocalDate logDate;

    @Column(nullable = false)
    private Double totalProtein;

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<FoodItem> getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    public LocalDate getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    public Double getTotalProtein() {
        return totalProtein;
    }

    public void setTotalProtein(Double totalProtein) {
        this.totalProtein = totalProtein;
    }

private NutritionLog(Builder builder) {
    this.user = builder.user;
    this.foodItems = builder.foodItems;
    this.logDate = builder.logDate;
    this.totalProtein = builder.totalProtein;
}

public static class Builder {
    private User user;
    private List<FoodItem> foodItems;
    private LocalDate logDate;
    private Double totalProtein;

    public Builder setUser(User user) {
        this.user = user;
        return this;
    }

    public Builder setFoodItems(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
        return this;
    }

    public Builder setLogDate(LocalDate logDate) {
        this.logDate = logDate;
        return this;
    }

    public Builder setTotalProtein(Double totalProtein) {
        this.totalProtein = totalProtein;
        return this;
    }

    public NutritionLog build() {
        return new NutritionLog(this);
    }
}
}
