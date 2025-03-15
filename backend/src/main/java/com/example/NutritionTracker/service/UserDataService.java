package com.example.NutritionTracker.service;

import com.example.NutritionTracker.dto.UserDTO;
import com.example.NutritionTracker.dto.UserDataDTO;
import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.repo.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
public class UserDataService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserDataService.class);

    public UserDataService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    public void saveUser(UserDataDTO userDataDTO) {
//
//        User user = new User();
//        user.setName(userDataDTO.getName());
//        user.setAge(userDataDTO.getAge());
//        user.setWeight(userDataDTO.getWeight());
//        user.setIsAthlete(userDataDTO.getIsAthlete());
//        user.setIsVegan(userDataDTO.getIsVegan());
//        user.setIsLongevityFocused(userDataDTO.getIsLongevityFocused());
//
//        userRepository.save(user);
//    }

    @Transactional(readOnly = true)
    public Optional<UserDTO> getUser() {
        return userRepository.findAll().stream()
                .findFirst()
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .age(user.getAge())
                        .weight(user.getWeight())
                        .isAthlete(user.getIsAthlete())
                        .isVegan(user.getIsVegan())
                        .isLongevityFocused(user.getIsLongevityFocused())
                        .build());
    }
    public User updateExistingUser(UserDataDTO userDataDTO) {

        System.out.println("✅ updateExistingUser() wurde aufgerufen für User: " + userDataDTO.getName());
        logger.info("ℹ️ Eingehende Benutzerdaten: {}", userDataDTO);
        logger.info("🎯 Eingehende Werte: Name={}, Age={}, Weight={}, Athlete={}, Vegan={}, Longevity={}",
                userDataDTO.getName(), userDataDTO.getAge(), userDataDTO.getWeight(),
                userDataDTO.getIsAthlete(), userDataDTO.getIsVegan(), userDataDTO.getIsLongevityFocused());
        // Lade den existierenden User aus der Datenbank
        User user = userRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Kein User in der Datenbank gefunden."));

        logger.info("📝 Gefundener Benutzer vor dem Update: {}", user);

        // Vorherige Werte ausgeben, um zu sehen, ob null-Werte reinkommen
        logger.info("⚠️ Vorheriger Zustand: Name={}, Age={}, Weight={}, Athlete={}, Vegan={}, Longevity={}",
                user.getName(), user.getAge(), user.getWeight(),
                user.getIsAthlete(), user.getIsVegan(), user.getIsLongevityFocused());

        // Neue Werte setzen
        user.setName(userDataDTO.getName());
        user.setAge(userDataDTO.getAge());
        user.setWeight(userDataDTO.getWeight());
        user.setIsAthlete(userDataDTO.getIsAthlete());
        user.setIsVegan(userDataDTO.getIsVegan());
        user.setIsLongevityFocused(userDataDTO.getIsLongevityFocused());

        // Nach dem Setzen prüfen, ob die Werte korrekt übernommen wurden
        logger.info("🆕 Neuer Zustand vor Speicherung: Name={}, Age={}, Weight={}, Athlete={}, Vegan={}, Longevity={}",
                user.getName(), user.getAge(), user.getWeight(),
                user.getIsAthlete(), user.getIsVegan(), user.getIsLongevityFocused());

        // Speichere User
        User savedUser = userRepository.save(user);

        // Ausgabe nach dem Speichern
        logger.info("✅ Gespeicherter Benutzer: {}", savedUser);
        logger.info("🆕 Neuer Zustand nach Speicherung: Name={}, Age={}, Weight={}, Athlete={}, Vegan={}, Longevity={}",
                user.getName(), user.getAge(), user.getWeight(),
                user.getIsAthlete(), user.getIsVegan(), user.getIsLongevityFocused());


        return savedUser;
    }
}