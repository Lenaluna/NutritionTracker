package com.example.NutritionTracker.api;

import com.example.NutritionTracker.dto.FoodItemDTO;
import com.example.NutritionTracker.service.FoodItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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