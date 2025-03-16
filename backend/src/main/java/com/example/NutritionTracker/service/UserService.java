package com.example.NutritionTracker.service;

import com.example.NutritionTracker.dto.UserDTO;
import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.repo.UserRepository;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service class for managing user-related operations.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    /**
     * Deletes all users from the database before application shutdown.
     */
    @PreDestroy
    public void cleanup() {
        logger.info("Cleaning up database before shutdown...");
        userRepository.deleteAll();
        logger.info("All users deleted.");
    }

    /**
     * Creates a new user in the database.
     * Ensures that only one user exists at a time.
     *
     * @param user The user entity to be created.
     * @return The created user entity.
     * @throws IllegalStateException if a user already exists in the database.
     */
    @Transactional
    public User createUser(User user) {
        if (userRepository.count() > 0) {
            throw new IllegalStateException("A user already exists. Only one user is allowed.");
        }
        return userRepository.save(user);
    }

    /**
     * Retrieves the first available user from the database.
     *
     * @return An Optional containing the user as a DTO, or empty if no user exists.
     */
    @Transactional(readOnly = true)
    public Optional<UserDTO> getSingleUser() {
        return userRepository.findAll().stream().findFirst()
                .map(user -> new UserDTO(
                        user.getId(),
                        user.getName(),
                        user.getAge(),
                        user.getWeight(),
                        user.getIsAthlete(),
                        user.getIsVegan(),
                        user.getIsLongevityFocused()
                ));
    }
}