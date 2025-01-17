
package com.example.NutritionTracker.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NutritionLogBuilderTest {

    @Test
    void testBuilderPattern() {
        NutritionLog log = new NutritionLog.Builder()
                .setUser(new User())
                .setFoodItems(Collections.emptyList())
                .setLogDate(LocalDate.now())
                .setTotalProtein(20.5)
                .build();

        assertNotNull(log);
        assertNotNull(log.getUser());
        assertEquals(Collections.emptyList(), log.getFoodItems());
        assertEquals(LocalDate.now(), log.getLogDate());
        assertEquals(20.5, log.getTotalProtein());
    }
}

