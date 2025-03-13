package com.example.NutritionTracker.api;

import com.example.NutritionTracker.service.AminoProfileService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/amino-profile")
@RequiredArgsConstructor
public class AminoProfileController {
    private final AminoProfileService aminoProfileService;
    private static final Logger logger = LoggerFactory.getLogger(AminoProfileController.class);

    /**
     * Calculates the sum of amino acids from the selected food items in a nutrition log.
     * @return a map of amino acids and their summed values
     */
    @PostMapping("/sum")
    public ResponseEntity<Map<String, Double>> calculateAminoAcidSum() {
        logger.info("Request zum Berechnen der Aminosäuren für das neueste Log erhalten.");

        Map<String, Double> aminoAcidSum = aminoProfileService.calculateAminoAcidSumsForLatestLog();

        return ResponseEntity.ok(aminoAcidSum);
    }

    /**
     * Retrieves the calculated daily amino acid needs based on user weight and decorators.
     * @return a map of amino acids and their daily required amounts
     */
    @GetMapping("/daily-needs")
    public ResponseEntity<Map<String, Double>> calculateDailyAminoNeeds() {
        logger.info("Received request to calculate daily amino acid needs.");
        Map<String, Double> dailyNeeds = aminoProfileService.calculateDailyAminoAcidNeeds();
        return ResponseEntity.ok(dailyNeeds);
    }

    /**
     * Calculates the amino acid coverage percentage based on daily needs and consumed food items.
     * @return a map of amino acids and their coverage percentage
     */
    @PostMapping("/coverage")
    public ResponseEntity<Map<String, Double>> calculateAminoAcidCoverage() {
        logger.info("Request zum Berechnen der Aminosäurenabdeckung für das neueste Log erhalten.");

        // Coverage berechnen mit dem neuesten NutritionLog
        Map<String, Double> coverage = aminoProfileService.calculateAminoAcidCoverageForLatestLog();

        return ResponseEntity.ok(coverage);
    }
}