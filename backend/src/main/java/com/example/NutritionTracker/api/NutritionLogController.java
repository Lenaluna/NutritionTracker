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
     * Creates a new NutritionLog for a user.
     *
     * The frontend only sends the user ID inside {@link NutritionLogCreateDTO},
     * meaning the NutritionLog is created for this user without any food items initially.
     *
     * @param logDTO The DTO containing the user ID for whom the NutritionLog is created.
     * @return ResponseEntity containing the created NutritionLog's ID and associated user ID.
     */
    @PostMapping("/create")
    public ResponseEntity<NutritionLogResponseDTO> createLog(@RequestBody NutritionLogCreateDTO logDTO) {
        NutritionLog savedLog = nutritionLogService.createLogFromFrontend(logDTO);
        return ResponseEntity.ok(new NutritionLogResponseDTO(savedLog.getId(), savedLog.getUser().getId()));
    }

    /**
     * Adds a food item to an existing NutritionLog.
     *
     * The specified food item is linked to the provided NutritionLog ID.
     *
     * @param logId The ID of the NutritionLog.
     * @param foodItemId The ID of the food item to be added.
     * @return ResponseEntity with HTTP status 201 (Created) if the food item was successfully added.
     */
    @PostMapping("/{logId}/food-items/{foodItemId}")
    public ResponseEntity<Void> addFoodItemToLog(@PathVariable UUID logId, @PathVariable UUID foodItemId) {
        nutritionLogService.addFoodItemToLog(logId, foodItemId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Retrieves a specific NutritionLog by its ID.
     *
     * @param id The ID of the requested NutritionLog.
     * @return ResponseEntity containing the NutritionLog if found, otherwise returns HTTP 404 (Not Found).
     */
    @GetMapping("/{id}")
    public ResponseEntity<NutritionLog> getNutritionLog(@PathVariable UUID id) {
        NutritionLog nutritionLog = nutritionLogService.getNutritionLogById(id);
        return ResponseEntity.ok(nutritionLog);
    }

    /**
     * Retrieves the latest NutritionLog from the database.
     *
     * This method fetches the most recent NutritionLog entry if available.
     *
     * @return ResponseEntity containing the latest NutritionLogDTO if found, otherwise HTTP 404 (Not Found).
     */
    @GetMapping("/latest")
    public ResponseEntity<NutritionLogDTO> getLatestNutritionLog() {
        return nutritionLogService.getLatestNutritionLog()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
