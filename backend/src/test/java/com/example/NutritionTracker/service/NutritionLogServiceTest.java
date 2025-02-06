
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

import java.time.LocalDate;
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
    private User normalUser;
    private User athleteUser;
    private User childUser;
    private FoodItem chicken;
    private FoodItem quinoa;

    @BeforeEach
    void setUp() {
        normalUser = User.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .age(30)
                .weight(75.0)
                .isAthlete(false)
                .build();

        athleteUser = User.builder()
                .id(UUID.randomUUID())
                .name("Jane Athlete")
                .age(25)
                .weight(65.0)
                .isAthlete(true)
                .build();

        childUser = User.builder()
                .id(UUID.randomUUID())
                .name("Tim")
                .age(10)
                .weight(30.0)
                .isAthlete(false)
                .build();

        chicken = FoodItem.builder()
                .id(UUID.randomUUID())
                .name("Chicken Breast")
                .proteinContent(31.0)
                .aminoAcidProfile(Map.of("Lysine", 2.5, "Valine", 3.0, "Isoleucine", 1.8))
                .build();

        quinoa = FoodItem.builder()
                .id(UUID.randomUUID())
                .name("Quinoa")
                .proteinContent(14.0)
                .aminoAcidProfile(Map.of("Lysine", 1.2, "Valine", 2.0, "Isoleucine", 1.5))
                .build();
    }

    @Test
    void testCalculateAminoAcidsForNormalUser() {
        log = NutritionLog.builder()
                .id(UUID.randomUUID())
                .user(normalUser)
                .foodItems(List.of(chicken, quinoa))
                .logDate(LocalDate.now())
                .totalProtein(45.0)
                .build();

        Map<String, Double> result = nutritionLogService.calculateAminoAcidsForLog(log);
        assertEquals(3.7, result.get("Lysine"), 0.01);
        assertEquals(5.0, result.get("Valine"), 0.01);
        assertEquals(3.3, result.get("Isoleucine"), 0.01);
    }

    @Test
    void testCalculateAminoAcidsForAthlete() {
        log = NutritionLog.builder()
                .id(UUID.randomUUID())
                .user(athleteUser)
                .foodItems(List.of(chicken, quinoa))
                .logDate(LocalDate.now())
                .totalProtein(45.0)
                .build();

        Map<String, Double> result = nutritionLogService.calculateAminoAcidsForLog(log);
        assertEquals(4.44, result.get("Lysine"), 0.01);
        assertEquals(6.0, result.get("Valine"), 0.01);
        assertEquals(3.96, result.get("Isoleucine"), 0.01);
    }

    @Test
    void testCalculateAminoAcidsForChild() {
        log = NutritionLog.builder()
                .id(UUID.randomUUID())
                .user(childUser)
                .foodItems(List.of(chicken, quinoa))
                .logDate(LocalDate.now())
                .totalProtein(45.0)
                .build();

        Map<String, Double> result = nutritionLogService.calculateAminoAcidsForLog(log);
        assertEquals(2.96, result.get("Lysine"), 0.01);
        assertEquals(4.0, result.get("Valine"), 0.01);
        assertEquals(2.64, result.get("Isoleucine"), 0.01);
    }

    @Test
    void testGetLogById() {
        UUID logId = UUID.randomUUID();
        log = NutritionLog.builder().id(logId).build();
        when(nutritionLogRepository.findById(logId)).thenReturn(Optional.of(log));

        Optional<NutritionLog> foundLog = nutritionLogService.getLogById(logId);
        assertTrue(foundLog.isPresent());
        assertEquals(logId, foundLog.get().getId());
    }

    @Test
    void testDeleteLog() {
        UUID logId = UUID.randomUUID();
        doNothing().when(nutritionLogRepository).deleteById(logId);

        nutritionLogService.deleteLog(logId);
        verify(nutritionLogRepository, times(1)).deleteById(logId);
    }
}
