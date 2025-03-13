package com.example.NutritionTracker.service;

import com.example.NutritionTracker.dto.UserDTO;
import com.example.NutritionTracker.dto.UserDataDTO;
import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.repo.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserDataService {

    private final UserRepository userRepository;

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
                        .build());
    }

    public User updateExistingUser(UserDataDTO userDataDTO) {
        User user = userRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Kein User in der Datenbank gefunden."));

        user.setName(userDataDTO.getName());
        user.setAge(userDataDTO.getAge());
        user.setWeight(userDataDTO.getWeight());
        user.setIsAthlete(userDataDTO.getIsAthlete());
        user.setIsVegan(userDataDTO.getIsVegan());
        user.setIsLongevityFocused(userDataDTO.getIsLongevityFocused());

        return userRepository.save(user);
    }
}