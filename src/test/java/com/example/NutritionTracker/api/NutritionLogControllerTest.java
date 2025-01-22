package com.example.NutritionTracker.api;

import com.example.NutritionTracker.entity.NutritionLog;
import com.example.NutritionTracker.service.NutritionLogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class NutritionLogControllerTest {

    @Mock
    private NutritionLogService nutritionLogService;

    @InjectMocks
    private NutritionLogController nutritionLogController;

    @Test
    void testGetAllLogs() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(nutritionLogController).build();

        when(nutritionLogService.getAllLogs()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/nutrition-logs"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}