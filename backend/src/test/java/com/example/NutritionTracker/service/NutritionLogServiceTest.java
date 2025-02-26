package com.example.NutritionTracker.service;

import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.entity.NutritionLogFoodItem;
import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.repo.NutritionLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NutritionLogServiceTest {

    @Mock
    private NutritionLogRepository nutritionLogRepository;

    @InjectMocks
    private NutritionLogService nutritionLogService;

    private NutritionLog log;
    private NutritionLogFoodItem logItem2;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .age(30)
                .weight(75.0)
                .isAthlete(false)
                .build();

        FoodItem chicken = FoodItem.builder()
                .id(UUID.randomUUID())
                .name("Chicken Breast")
                .aminoAcidProfile(Map.of("Lysine", 2.5, "Valine", 3.0))
                .build();

        FoodItem quinoa = FoodItem.builder()
                .id(UUID.randomUUID())
                .name("Quinoa")
                .aminoAcidProfile(Map.of("Lysine", 1.2, "Valine", 2.0))
                .build();

        log = NutritionLog.builder()
                .id(UUID.randomUUID())
                .user(user)
                .logDateTime(LocalDateTime.now())
                .build();

        NutritionLogFoodItem logItem1 = new NutritionLogFoodItem(log, chicken);
        logItem2 = new NutritionLogFoodItem(log, quinoa);

        log.setFoodItems(List.of(logItem1, logItem2)); // Korrekte Speicherung
    }

    @Test
    void shouldGetLogById() {
        UUID logId = log.getId();
        when(nutritionLogRepository.findById(logId)).thenReturn(Optional.of(log));

        Optional<NutritionLog> foundLog = nutritionLogService.getLogById(logId);

        assertTrue(foundLog.isPresent());
        assertEquals(logId, foundLog.get().getId());
        assertEquals(2, foundLog.get().getFoodItems().size());
    }

    @Test
    void shouldDeleteLog() {
        // Arrange
        UUID logId = log.getId();
        when(nutritionLogRepository.existsById(logId)).thenReturn(true);

        // Act
        nutritionLogService.deleteLog(logId);

        // Assert
        verify(nutritionLogRepository).existsById(logId);
        verify(nutritionLogRepository).deleteById(logId);
    }

    @Test
    void shouldReturnAllLogs() {
        when(nutritionLogRepository.findAll()).thenReturn(List.of(log));

        List<NutritionLog> logs = nutritionLogService.getAllLogs();

        assertFalse(logs.isEmpty());
        assertEquals(1, logs.size());
        assertEquals(2, logs.get(0).getFoodItems().size()); // Prüfen, ob die FoodItems korrekt geladen wurden
    }
}