package com.example.NutritionTracker.api;

import com.example.NutritionTracker.dto.UserDataDTO;
import com.example.NutritionTracker.service.UserDataService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/user-data")
@CrossOrigin(origins = "http://localhost:5173")

public class UserDataController {

    private final UserDataService userDataService;

    public UserDataController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveUserData(@Valid @RequestBody UserDataDTO userData) {
        userDataService.saveUser(userData);
        return ResponseEntity.ok("User gespeichert.");
    }

    @GetMapping
    public ResponseEntity<UserDataDTO> getUserData() {
        UserDataDTO userData = userDataService.getUser();
        return ResponseEntity.ok(userData);
    }
}