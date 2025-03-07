package com.example.NutritionTracker.service;

import com.example.NutritionTracker.dto.UserDataDTO;
import org.springframework.stereotype.Service;

@Service
public class UserDataService {

    private UserDataDTO currentUser;

    public void saveUser(UserDataDTO userData) {
        this.currentUser = userData; // User speichern
    }

    public UserDataDTO getUser() {
        return this.currentUser; // Gibt den gespeicherten User zurück
    }
}