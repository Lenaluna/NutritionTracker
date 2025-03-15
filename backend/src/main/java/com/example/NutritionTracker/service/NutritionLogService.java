package com.example.NutritionTracker.service;


import com.example.NutritionTracker.dto.NutritionLogCreateDTO;
import com.example.NutritionTracker.dto.NutritionLogDTO;
import com.example.NutritionTracker.dto.NutritionLogFoodItemDTO;
import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.entity.NutritionLogFoodItem;
import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.repo.FoodItemRepository;
import com.example.NutritionTracker.repo.NutritionLogFoodItemRepository;
import com.example.NutritionTracker.repo.NutritionLogRepository;
import com.example.NutritionTracker.repo.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NutritionLogService {

    private static final Logger logger = LoggerFactory.getLogger(NutritionLogService.class);

    private final NutritionLogRepository nutritionLogRepository;
    private final FoodItemRepository foodItemRepository;
    private final NutritionLogFoodItemRepository nutritionLogFoodItemRepository;
    private final UserRepository userRepository; // Ensure this is defined

    @Transactional
    public void cleanup() {
        logger.info("Cleaning up database before shutdown...");
        nutritionLogRepository.deleteAll();
        logger.info("All nutrition logs deleted.");
    }

    @Transactional(readOnly = true)
    public List<NutritionLog> getAllLogs() {
        return nutritionLogRepository.findAll();
    }

    @Transactional
    public void addFoodItemToLog(UUID logId, UUID foodItemId) {
        NutritionLog nutritionLog = nutritionLogRepository.findById(logId)
                .orElseThrow(() -> new EntityNotFoundException("NutritionLog not found with ID: " + logId));

        FoodItem foodItem = foodItemRepository.findById(foodItemId)
                .orElseThrow(() -> new EntityNotFoundException("FoodItem not found with ID: " + foodItemId));

        // Prüfen, ob die Kombination aus NutritionLog und FoodItem bereits existiert
//        boolean exists = nutritionLogFoodItemRepository.findByNutritionLogIdAndFoodItemId(logId, foodItemId).isPresent();
//        if (exists) {
//            logger.warn("FoodItem {} ist bereits im NutritionLog {} vorhanden.", foodItemId, logId);
//            return;
//        }
//
//        if (nutritionLog.getFoodItems() == null) {
//            nutritionLog.setFoodItems(new ArrayList<>());
//            logger.warn("foodItems list war null für NutritionLog mit ID: {}. Neue Liste initialisiert.", logId);
//        }

        // Überprüfung, ob das FoodItem schon existiert
        if (nutritionLog.getFoodItems().stream().anyMatch(item -> item.getFoodItem().getId().equals(foodItemId))) {
            logger.warn("FoodItem {} ist bereits im NutritionLog {} vorhanden.", foodItemId, logId);
            return;
        }

        NutritionLogFoodItem logFoodItem = new NutritionLogFoodItem();
        logFoodItem.setNutritionLog(nutritionLog);
        logFoodItem.setFoodItem(foodItem);

        nutritionLog.getFoodItems().add(logFoodItem);
        nutritionLogFoodItemRepository.save(logFoodItem);

        nutritionLogRepository.save(nutritionLog);

        logger.info("Added FoodItem {} to NutritionLog {}", foodItemId, logId);
    }

    @Transactional
    public NutritionLog createLog(NutritionLog log) {
        NutritionLog savedLog = nutritionLogRepository.save(log);

        // Überprüfung, ob es wirklich gespeichert wurde
        savedLog = nutritionLogRepository.findById(savedLog.getId()).orElse(null);

        if (savedLog == null || savedLog.getId() == null) {
            throw new IllegalStateException("Fehler beim Speichern von NutritionLog.");
        }

        return savedLog;
    }
//    public NutritionLog createLog(NutritionLog log) {
//        return nutritionLogRepository.save(log);
//    }

    @Transactional(readOnly = true)
    public NutritionLog getNutritionLogById(UUID logId) {
        if (logId == null) {
            logger.error("Die übergebene logId (UUID) ist null. Bitte eine gültige UUID angeben.");
            throw new IllegalArgumentException("Die logId darf nicht null sein!");
        }

        logger.info("Versuche, NutritionLog mit logId (UUID): {} aus der Datenbank abzurufen.", logId);

        return nutritionLogRepository.findById(logId)
                .map(nutritionLog -> {
                    logger.info("NutritionLog erfolgreich gefunden: {}", nutritionLog);
                    return nutritionLog;
                })
                .orElseThrow(() -> {
                    logger.warn("NutritionLog mit logId (UUID): {} wurde nicht gefunden.", logId);
                    return new IllegalArgumentException("NutritionLog mit der UUID " + logId + " wurde nicht gefunden.");
                });
    }

    public NutritionLogDTO convertToDTO(NutritionLog nutritionLog) {
        return NutritionLogDTO.builder()
                .id(nutritionLog.getId())
                .userId(nutritionLog.getUser().getId())
                .foodItems(nutritionLog.getFoodItems().stream()
                        .map(item -> new NutritionLogFoodItemDTO(item.getId(), item.getFoodItem().getId(), nutritionLog.getId()))
                        .collect(Collectors.toList()))
                .build();
    }

//    @Transactional
//    public NutritionLog createLogFromFrontend(NutritionLogCreateDTO logDTO) {
//
//        if (logDTO.getUserId() == null) {
//            throw new IllegalArgumentException("User ID darf nicht null sein!");
//        }
//
//
//        User user = userRepository.findById(logDTO.getUserId())
//                .orElseThrow(() -> new EntityNotFoundException("User mit ID " + logDTO.getUserId() + " nicht gefunden"));
//
//        NutritionLog log = NutritionLog.builder()
//                .user(user)
//                .build();
//
//        return nutritionLogRepository.save(log);
//    }

    @Transactional
    public NutritionLog createLogFromFrontend(NutritionLogCreateDTO logDTO) {
        logger.info("📝 Anfrage zum Erstellen eines NutritionLogs erhalten: {}", logDTO);

        if (logDTO.getUserId() == null) {
            logger.error("❌ Fehler: User ID ist null! NutritionLog kann nicht erstellt werden.");
            throw new IllegalArgumentException("User ID darf nicht null sein!");
        }

        logger.info("🔍 Suche nach Benutzer mit ID: {}", logDTO.getUserId());

        // Benutzer aus der Datenbank abrufen
        User user = userRepository.findById(logDTO.getUserId())
                .orElseThrow(() -> {
                    logger.error("❌ Benutzer mit ID {} nicht gefunden!", logDTO.getUserId());
                    return new EntityNotFoundException("User mit ID " + logDTO.getUserId() + " nicht gefunden");
                });

        logger.info("✅ Benutzer gefunden: {} (ID: {})", user.getName(), user.getId());

        // Neues NutritionLog für den Benutzer erstellen
        NutritionLog log = NutritionLog.builder()
                .user(user)
                .build();

        // Speichern des NutritionLogs
        NutritionLog savedLog = nutritionLogRepository.save(log);

        // Überprüfung nach dem Speichern
        if (savedLog.getId() == null) {
            logger.error("❌ Fehler beim Speichern des NutritionLogs! Das Objekt hat keine ID erhalten.");
            throw new IllegalStateException("Fehler beim Speichern von NutritionLog.");
        }

        logger.info("✅ NutritionLog erfolgreich erstellt mit ID: {}", savedLog.getId());

        return savedLog;
    }

    @Transactional(readOnly = true)
    public Optional<NutritionLogDTO> getLatestNutritionLog() {
        return nutritionLogRepository.findTopByOrderByIdDesc()
                .map(NutritionLogDTO::new); // Wandelt NutritionLog in NutritionLogDTO um, falls vorhanden
    }

    @Transactional(readOnly = true)
    public Optional<NutritionLog> getLatestNutritionLogEntity() {
        return nutritionLogRepository.findTopByOrderByIdDesc(); // Gibt direkt die Entity zurück
    }
}

