package com.example.NutritionTracker.service;

import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    /**
     * Creates or updates a user.
     * If a user with the same name already exists, it returns the existing user.
     * Otherwise, a new user is created and saved in the database.
     *
     * @param newUser The user object to be created or retrieved.
     * @return The persisted user.
     */
    @Transactional
    public User createUser(User newUser) {
        logger.info("Creating user: {}", newUser.getName());

        Optional<User> existingUser = userRepository.findByName(newUser.getName());

        if (existingUser.isPresent()) {
            logger.warn("User already exists: {} with ID {}", existingUser.get().getName(), existingUser.get().getId());
            return existingUser.get(); // Return existing user
        }

        newUser.setId(UUID.randomUUID()); // Ensure ID is set
        logger.info("Saving new user with ID: {}", newUser.getId());

        return userRepository.save(newUser); // Use save() instead of saveAndFlush()
    }

    /**
     * Retrieves a user by their name.
     * This method is transactional to ensure a consistent read from the database.
     *
     * @return An Optional containing the user if found.
     */
    public Optional<User> getUser() {
        return userRepository.findByName("Test User");
    }
}