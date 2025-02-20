package com.example.NutritionTracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;  // Unique identifier, only required if using a database

    @NotNull
    @Size(min = 3, max = 50)
    private String name;  // User's name, must be between 3 and 50 characters

    @Min(0) @Max(150)
    private Integer age;  // User's age, restricted between 0 and 150

    @Min(1) @Max(500)
    private Double weight;  // User's weight, between 1 and 500 kg

    private Boolean isAthlete;  // Indicates whether the user is an athlete

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NutritionLog> nutritionLogs;


}