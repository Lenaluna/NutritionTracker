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
        if (userRepository.count() > 0) {
            throw new IllegalStateException("Ein Benutzer ist bereits vorhanden. Es kann nur ein Benutzer existieren.");
        }
        return userRepository.save(user);
    }

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