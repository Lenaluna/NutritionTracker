package com.example.NutritionTracker.service;

import com.example.NutritionTracker.dto.UserDTO;
import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.repo.UserRepository;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
        if (userRepository.count() > 0) {
            throw new IllegalStateException("Ein Benutzer ist bereits vorhanden. Es kann nur ein Benutzer existieren.");
        }
        return userRepository.save(user);
    }

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

    public List<User> getAllUsers() {
        return userRepository.findAll();
       }

    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }

    @Transactional(readOnly = true)
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + id + " was not found."));
    }

    @Transactional
    public UserDTO saveOrUpdateUser(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findById(userDTO.getId());

        User user = existingUser.orElseGet(User::new);
        user.setName(userDTO.getName());
        user.setAge(userDTO.getAge());
        user.setWeight(userDTO.getWeight());
        user.setIsAthlete(userDTO.getIsAthlete());

        User savedUser = userRepository.save(user);

        return UserDTO.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .age(savedUser.getAge())
                .weight(savedUser.getWeight())
                .isAthlete(savedUser.getIsAthlete())
                .build();
    }
}