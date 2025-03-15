package com.example.NutritionTracker.api;

import com.example.NutritionTracker.dto.NutritionLogCreateDTO;
import com.example.NutritionTracker.dto.NutritionLogDTO;
import com.example.NutritionTracker.dto.NutritionLogResponseDTO;
import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.service.NutritionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequestMapping("/nutrition-logs")
@RequiredArgsConstructor
public class NutritionLogController {

    private final NutritionLogService nutritionLogService;

    /**
     * Creates a new NutritionLog.
     *
     * The frontend only sends the user ID inside `NutritionLogCreateDTO`,
     * meaning the NutritionLog is created for this user without any food items yet.
     *
     * In the response, the backend returns the generated NutritionLog ID and the associated user ID.
     * The food items list is not included because food items are added later.
     */
    @PostMapping("/create")
    public ResponseEntity<NutritionLogResponseDTO> createLog(@RequestBody NutritionLogCreateDTO logDTO) {
        NutritionLog savedLog = nutritionLogService.createLogFromFrontend(logDTO);
        return ResponseEntity.ok(new NutritionLogResponseDTO(savedLog.getId(), savedLog.getUser().getId()));
    }

    @PostMapping("/{logId}/food-items/{foodItemId}")
    public ResponseEntity<Void> addFoodItemToLog(@PathVariable UUID logId, @PathVariable UUID foodItemId) {
        nutritionLogService.addFoodItemToLog(logId, foodItemId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NutritionLog> getNutritionLog(@PathVariable UUID id) {
        NutritionLog nutritionLog = nutritionLogService.getNutritionLogById(id);
        return ResponseEntity.ok(nutritionLog);
    }

    /**
     * Retrieving the Latest Nutrition Log from the Database.
     */
    @GetMapping("/latest")
    public ResponseEntity<NutritionLogDTO> getLatestNutritionLog() {
        return nutritionLogService.getLatestNutritionLog()
                .map(ResponseEntity::ok) // ✅ Direkt zurückgeben, weil es schon ein DTO ist
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
