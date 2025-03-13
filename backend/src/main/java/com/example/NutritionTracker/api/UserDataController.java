package com.example.NutritionTracker.api;

import com.example.NutritionTracker.dto.UserDTO;
import com.example.NutritionTracker.dto.UserDataDTO;
import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.service.UserDataService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-data")
public class UserDataController {

    private final UserDataService userDataService;

    public UserDataController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    /**
     * Updates the existing user with new values from the frontend.
     * If the user does not exist, an exception is thrown.
     * Used to update properties such as name or weight.
     *
     * @param userData The updated user data from the frontend.
     * @return The updated user as a ResponseEntity.
     */
    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@Valid @RequestBody UserDataDTO userData) {
        User updatedUser = userDataService.updateExistingUser(userData);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * Retrieves the current user from the database.
     * This API is used by the frontend to display the user's name.
     *
     * @return The current user as a DTO if found, otherwise 404 Not Found.
     */
    @GetMapping("/current")
    public ResponseEntity<UserDTO> getCurrentUser() {
        return userDataService.getUser()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}