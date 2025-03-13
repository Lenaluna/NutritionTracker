package com.example.NutritionTracker.api;

import com.example.NutritionTracker.dto.FoodItemDTO;
import com.example.NutritionTracker.service.FoodItemService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/food-items")
@RequiredArgsConstructor
public class FoodItemController {
    private final FoodItemService foodItemService;


    /**
     * Retrieves all available food items as DTOs to prevent Lazy-Loading issues.
     * Converts FoodItem entities into FoodItemDTOs before returning them.
     *
     * @return List of all food items as DTOs.
     */
    @GetMapping("/all")
    public ResponseEntity<List<FoodItemDTO>> getAllFoodItems() {
        List<FoodItemDTO> foodItems = foodItemService.getAllFoodItems();
        return ResponseEntity.ok(foodItems);
    }
}