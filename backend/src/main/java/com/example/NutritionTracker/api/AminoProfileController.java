package com.example.NutritionTracker.api;


import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.repo.FoodItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.UUID;

@RestController
@RequestMapping("/amino-profile")
@RequiredArgsConstructor
public class AminoProfileController {

    private final FoodItemRepository foodItemRepository;
    private static final Logger logger = LoggerFactory.getLogger(AminoProfileController.class);


    @PostMapping("/calculate")
    public ResponseEntity<AminoProfileResponse> calculateAminoProfile(@RequestBody AminoProfileRequest request) {
        System.out.println("AminoProfileController aufgerufen");

        logger.info("Empfangene Anfrage: {}", request);

        UserData userData = request.getUserData();
        List<UUID> foodIds = request.getSelectedFoodIds();

        logger.info("User-Daten: {}, Ausgewählte Lebensmittel-IDs: {}", userData, foodIds);

        // Lebensmittel aus der Datenbank laden
        List<FoodItem> selectedFoods = foodItemRepository.findAllById(foodIds);
        logger.info("Geladene Lebensmittel: {}", selectedFoods);

        // Aminosäuren berechnen
        Map<String, Double> sumAminos = calculateAminoAcidSums(selectedFoods);
        logger.info("Summierte Aminosäuren: {}", sumAminos);

        // Tagesbedarf berechnen
        Map<String, Double> dailyCoverage = calculateDailyNeeds(sumAminos, userData);
        logger.info("Tagesbedarf Deckung: {}", dailyCoverage);

        // Antwort erstellen
        AminoProfileResponse response = new AminoProfileResponse();
        response.setAminoAcids(sumAminos);
        response.setDailyCoverage(dailyCoverage);

        logger.debug("UserData: {}", request.getUserData());
        logger.debug("Selected Food IDs: {}", request.getSelectedFoodIds());
        logger.debug("Calculated Amino Acids: {}", response.getAminoAcids());
        logger.debug("Daily Coverage before returning: {}", response.getDailyCoverage());

        return ResponseEntity.ok(response);
    }

    /** Hilfsmethode: Summiert die Aminosäurenwerte aller gewählten Lebensmittel */
    private Map<String, Double> calculateAminoAcidSums(List<FoodItem> foodItems) {
        Map<String, Double> sumAminos = new HashMap<>();
        for (FoodItem item : foodItems) {
            if (item.getAminoAcidProfile() != null) {
                item.getAminoAcidProfile().forEach((amino, value) ->
                        sumAminos.merge(amino, value, Double::sum)
                );
            }
        }
        return sumAminos;
    }

    /** Hilfsmethode: Berechnet die prozentuale Deckung des Tagesbedarfs */
    private Map<String, Double> calculateDailyNeeds(Map<String, Double> sumAminos, UserData userData) {
        Map<String, Double> dailyNeeds = getRecommendedDailyAminoNeeds(userData);
        Map<String, Double> dailyCoverage = new HashMap<>();

        for (String amino : dailyNeeds.keySet()) {
            double requiredAmount = dailyNeeds.get(amino);
            double consumedAmount = sumAminos.getOrDefault(amino, 0.0);
            double percentage = (consumedAmount / requiredAmount) * 100;
            dailyCoverage.put(amino, Math.min(percentage, 100.0)); // Maximal 100%
        }

        return dailyCoverage;
    }

    /** Beispielhafte Tagesbedarfswerte (könnten in einer DB oder Config-Datei stehen) */
    private Map<String, Double> getRecommendedDailyAminoNeeds(UserData userData) {
        Map<String, Double> dailyNeeds = new HashMap<>();

        // Beispielwerte für Aminosäurenbedarf in mg/kg Körpergewicht
        double baseWeight = userData.getWeight();
        dailyNeeds.put("Lysin", 38 * baseWeight);
        dailyNeeds.put("Methionin", 15 * baseWeight);
        dailyNeeds.put("Threonin", 23 * baseWeight);
        dailyNeeds.put("Valin", 39 * baseWeight);
        // ... weitere Werte hinzufügen

        return dailyNeeds;
    }
}