package com.example.NutritionTracker.api;

import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.service.NutritionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/nutrition-logs")
@RequiredArgsConstructor
public class NutritionLogController {

    private final NutritionLogService nutritionLogService;

    @GetMapping
    public List<NutritionLog> getAllLogs() {
        return nutritionLogService.getAllLogs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NutritionLog> getLogById(@PathVariable UUID id) {
        Optional<NutritionLog> log = nutritionLogService.getLogById(id);
        return log.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<NutritionLog> createLog(@RequestBody NutritionLog log) {
        NutritionLog savedLog = nutritionLogService.createLog(log);
        return ResponseEntity.ok(savedLog);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable UUID id) {
        nutritionLogService.deleteLog(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<NutritionLog> updateLog(@PathVariable UUID id, @RequestBody NutritionLog log) {
        NutritionLog updatedLog = nutritionLogService.updateLog(id, log);
        return ResponseEntity.ok(updatedLog);
    }

    @DeleteMapping("/{logId}/food-item/{foodItemId}")
    public ResponseEntity<Void> removeFoodItemFromLog(@PathVariable UUID logId, @PathVariable UUID foodItemId) {
        nutritionLogService.removeFoodItemFromLog(logId, foodItemId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{logId}/food-items/{foodItemId}")
    public ResponseEntity<Void> addFoodItemToLog(@PathVariable UUID logId, @PathVariable UUID foodItemId) {
        nutritionLogService.addFoodItemToLog(logId, foodItemId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("/calculate-amino-acids")
    public ResponseEntity<Map<String, Double>> calculateAminoAcids(@RequestBody NutritionLog log) {
        Map<String, Double> aminoAcids = nutritionLogService.calculateAminoAcidsForLog(log);
        return ResponseEntity.ok(aminoAcids);
    }
}
