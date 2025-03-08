package com.example.NutritionTracker.api;

import com.example.NutritionTracker.dto.FoodItemDTO;
import com.example.NutritionTracker.service.FoodItemService;
import jakarta.persistence.EntityNotFoundException;
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
    public ResponseEntity<List<FoodItemDTO>> getAllFoodItems() {
        List<FoodItemDTO> foodItems = foodItemService.getAllFoodItems();
        return ResponseEntity.ok(foodItems);
    }

    @GetMapping("/search")
    public ResponseEntity<List<FoodItemDTO>> searchByNameOrPartial(@RequestParam String name) {
        List<FoodItemDTO> foodItems = foodItemService.findByNameContainingIgnoreCase(name);
        if (foodItems.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(foodItems);
    }

    /** Retrieves a single food item by ID */
    @GetMapping("/{id}")
    public ResponseEntity<FoodItemDTO> getFoodItemById(@PathVariable UUID id) {
        Optional<FoodItemDTO> foodItemDTO = foodItemService.getFoodItemById(id);
        return foodItemDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FoodItemDTO> addFoodItem(@RequestBody FoodItemDTO foodItemDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(foodItemService.saveFoodItem(foodItemDTO));
    }

    /** Updates an existing food item */
    @PutMapping("/{id}")
    public ResponseEntity<FoodItemDTO> updateFoodItem(@PathVariable UUID id, @RequestBody FoodItemDTO updatedItem) {
        Optional<FoodItemDTO> updated = foodItemService.updateFoodItem(id, updatedItem);
        return updated.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /** Deletes a food item */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFoodItem(@PathVariable UUID id) {
        try {
            foodItemService.deleteFoodItem(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}