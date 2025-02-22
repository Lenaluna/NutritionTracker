package com.example.NutritionTracker.service;

import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.repo.UserRepository;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.NutritionTracker.exception.UserAlreadyExistsException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;



    @PreDestroy
    public void cleanup() {
        logger.info("Cleaning up database before shutdown...");
        userRepository.deleteAll();
        logger.info("All users deleted.");
    }

    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /**
     * Retrieves a user by their name.
     *
     * @param name The name of the user to find.
     * @return An Optional containing the user if found.
     */
    public Optional<User> findByName(String name) {
        return userRepository.findByName(name);
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