package com.example.NutritionTracker.api;

import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.service.NutritionLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Map;

@RestController
@RequestMapping("/api/nutrition-logs")
public class NutritionLogController {


    private final NutritionLogService nutritionLogService;

    // Constructor Injection
    public NutritionLogController(NutritionLogService nutritionLogService) {
        this.nutritionLogService = nutritionLogService;
    }
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
    public NutritionLog createLog(@RequestBody NutritionLog log) {
        return nutritionLogService.createLog(log);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLog(@PathVariable UUID id) {
        nutritionLogService.deleteLog(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/analysis")
    public ResponseEntity<Map<String, Object>> analyzeLog(@PathVariable UUID id) {
        Optional<NutritionLog> log = nutritionLogService.getLogById(id);
        return log.map(l -> ResponseEntity.ok(nutritionLogService.analyzeLog(l)))
                .orElse(ResponseEntity.notFound().build());
    }
}
