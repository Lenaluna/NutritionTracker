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

/**
 * Service class for managing user data.
 */
@Service
public class UserDataService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserDataService.class);

    public UserDataService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Retrieves the first available user from the database.
     *
     * @return An Optional containing the user as a DTO, or empty if no user exists.
     */
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

    /**
     * Updates an existing user with new data from the provided UserDataDTO.
     * If no user exists in the database, an exception is thrown.
     *
     * @param userDataDTO The DTO containing updated user data.
     * @return The updated User entity.
     */
    public User updateExistingUser(UserDataDTO userDataDTO) {
        logger.info("Incoming user data: {}", userDataDTO);
        logger.info("Incoming values: Name={}, Age={}, Weight={}, Athlete={}, Vegan={}, Longevity={}",
                userDataDTO.getName(), userDataDTO.getAge(), userDataDTO.getWeight(),
                userDataDTO.getIsAthlete(), userDataDTO.getIsVegan(), userDataDTO.getIsLongevityFocused());

        // Load the existing user from the database
        User user = userRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No user found in the database."));

        logger.info("Existing user before update: {}", user);

        // Log previous values before updating
        logger.info("Previous state: Name={}, Age={}, Weight={}, Athlete={}, Vegan={}, Longevity={}",
                user.getName(), user.getAge(), user.getWeight(),
                user.getIsAthlete(), user.getIsVegan(), user.getIsLongevityFocused());

        // Update user properties
        user.setName(userDataDTO.getName());
        user.setAge(userDataDTO.getAge());
        user.setWeight(userDataDTO.getWeight());
        user.setIsAthlete(userDataDTO.getIsAthlete());
        user.setIsVegan(userDataDTO.getIsVegan());
        user.setIsLongevityFocused(userDataDTO.getIsLongevityFocused());

        // Log new values before saving
        logger.info("New state before saving: Name={}, Age={}, Weight={}, Athlete={}, Vegan={}, Longevity={}",
                user.getName(), user.getAge(), user.getWeight(),
                user.getIsAthlete(), user.getIsVegan(), user.getIsLongevityFocused());

        // Save the updated user
        User savedUser = userRepository.save(user);

        // Log after saving
        logger.info("Saved user: {}", savedUser);
        logger.info("New state after saving: Name={}, Age={}, Weight={}, Athlete={}, Vegan={}, Longevity={}",
                user.getName(), user.getAge(), user.getWeight(),
                user.getIsAthlete(), user.getIsVegan(), user.getIsLongevityFocused());

        return savedUser;
    }
}