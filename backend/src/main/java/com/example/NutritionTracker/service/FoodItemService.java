package com.example.NutritionTracker.service;

import com.example.NutritionTracker.dto.FoodItemDTO;
import com.example.NutritionTracker.entity.FoodItem;
import com.example.NutritionTracker.repo.FoodItemRepository;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;
    private static final Logger logger = LoggerFactory.getLogger(FoodItemService.class);

    /** Returns all FoodItems as DTOs */
    @Transactional(readOnly = true)
    public List<FoodItemDTO> getAllFoodItems() {
        return foodItemRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @PreDestroy
    public void cleanup() {
        logger.info("Cleaning up food items from database...");
        foodItemRepository.deleteAll();
        logger.info("All food items deleted.");
    }

    /** Retrieves a FoodItem by ID */
    @Transactional(readOnly = true)
    public Optional<FoodItemDTO> getFoodItemById(UUID id) {
        return foodItemRepository.findById(id)
                .map(this::convertToDTO);
    }

    /** Saves a new FoodItem */
    @Transactional
    public FoodItemDTO saveFoodItem(FoodItemDTO foodItemDTO) {
        FoodItem foodItem = convertToEntity(foodItemDTO);
        return convertToDTO(foodItemRepository.save(foodItem));
    }

    @Transactional
    public List<FoodItemDTO> saveAllFoodItems(List<FoodItemDTO> foodItemsDTO) {
        List<FoodItem> foodItems = foodItemsDTO.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
        return foodItemRepository.saveAll(foodItems).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /** Updates an existing FoodItem */
    @Transactional
    public Optional<FoodItemDTO> updateFoodItem(UUID id, FoodItemDTO updatedFoodItem) {
        return foodItemRepository.findById(id).map(existingItem -> {
            existingItem.setName(updatedFoodItem.getName());
            existingItem.setAminoAcidProfile(updatedFoodItem.getAminoAcidProfile());
            FoodItem savedItem = foodItemRepository.save(existingItem);
            return new FoodItemDTO(savedItem.getId(), savedItem.getName(), savedItem.getAminoAcidProfile());
        });
    }

    /** Finds FoodItems by name containing a specific string (case-insensitive) */
    @Transactional(readOnly = true)
    public List<FoodItemDTO> findByNameContainingIgnoreCase(String namePart) {
        return foodItemRepository.findByNameContainingIgnoreCase(namePart).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /** Deletes a FoodItem by ID */
    @Transactional
    public void deleteFoodItem(UUID foodItemId) {
        if (!foodItemRepository.existsById(foodItemId)) {
            throw new EntityNotFoundException("FoodItem with ID " + foodItemId + " not found");
        }
        foodItemRepository.deleteById(foodItemId);
    }

    /** Converts a FoodItem entity to a DTO */
    private FoodItemDTO convertToDTO(FoodItem foodItem) {
        return new FoodItemDTO(foodItem.getId(), foodItem.getName(), foodItem.getAminoAcidProfile());
    }

    /** Converts a FoodItemDTO to an entity without setting an ID */
    private FoodItem convertToEntity(FoodItemDTO foodItemDTO) {
        return new FoodItem(foodItemDTO.getName(), foodItemDTO.getAminoAcidProfile());
    }
}