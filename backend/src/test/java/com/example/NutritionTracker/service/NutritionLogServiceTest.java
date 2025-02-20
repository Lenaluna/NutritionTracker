package com.example.NutritionTracker.service;

import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.entity.NutritionLog;
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
    private User user;
    private FoodItem chicken;
    private FoodItem quinoa;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .name("Test User")
                .age(30)
                .weight(75.0)
                .isAthlete(false)
                .build();

        chicken = FoodItem.builder()
                .id(UUID.randomUUID())
                .name("Chicken Breast")
                .aminoAcidProfile(Map.of("Lysine", 2.5, "Valine", 3.0))
                .build();

        quinoa = FoodItem.builder()
                .id(UUID.randomUUID())
                .name("Quinoa")
                .aminoAcidProfile(Map.of("Lysine", 1.2, "Valine", 2.0))
                .build();

        log = NutritionLog.builder()
                .id(UUID.randomUUID())
                .user(user)
                .foodItems(List.of(chicken, quinoa))
                .logDateTime(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldGetLogById() {
        UUID logId = log.getId();
        when(nutritionLogRepository.findById(logId)).thenReturn(Optional.of(log));

        Optional<NutritionLog> foundLog = nutritionLogService.getLogById(logId);

        assertTrue(foundLog.isPresent());
        assertEquals(logId, foundLog.get().getId());
    }

    @Test
    void shouldDeleteLog() {
        // Arrange
        UUID logId = UUID.fromString("e4178ca7-0d6b-4b8b-8932-927f87fb9567");
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
    }
}