package com.example.NutritionTracker.api;

import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.service.FoodItemService;
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


    /** Returns all available food items */
    @GetMapping
    public ResponseEntity<List<FoodItem>> getAllFoodItems() {
        List<FoodItem> foodItems = foodItemService.getAllFoodItems();
        return ResponseEntity.ok(foodItems);
    }

    @GetMapping("/search")
    public ResponseEntity<List<FoodItem>> searchByNameOrPartial(@RequestParam String name) {
        List<FoodItem> foodItems = foodItemService.findByNameContainingIgnoreCase(name);
        if (foodItems.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(foodItems);
    }

    /** Retrieves a single food item by ID */
    @GetMapping("/{id}")
    public ResponseEntity<FoodItem> getFoodItemById(@PathVariable UUID id) {
        Optional<FoodItem> foodItem = foodItemService.getFoodItemById(id);
        return foodItem.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /** Adds a new food item */
    @PostMapping
    public ResponseEntity<FoodItem> addFoodItem(@RequestBody FoodItem foodItem) {
        FoodItem savedItem = foodItemService.saveFoodItem(foodItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
    }

    /** Updates an existing food item */
    @PutMapping("/{id}")
    public ResponseEntity<FoodItem> updateFoodItem(@PathVariable UUID id, @RequestBody FoodItem updatedItem) {
        Optional<FoodItem> updated = foodItemService.updateFoodItem(id, updatedItem);
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /** Deletes a food item */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFoodItem(@PathVariable UUID id) {
        foodItemService.deleteFoodItem(id);
    }
}