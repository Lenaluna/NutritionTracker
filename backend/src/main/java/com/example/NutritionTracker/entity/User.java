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
    private String name;

    private Integer age;

    private Double weight;

    private Boolean isAthlete;  // Indicates whether the user is an athlete

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NutritionLog> nutritionLogs;

    @Version
    private Long version;
}