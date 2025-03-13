package com.example.NutritionTracker.api;

import com.example.NutritionTracker.dto.UserDTO;
import com.example.NutritionTracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user") // Singular because there is only one user
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

//    /**
//     * Creates a new user in the database.
//     * This only works if no user already exists (since only one user is allowed).
//     * Used in DevDataLoader during application startup.
//     *
//     * @param user The user entity containing properties like name, weight, etc.
//     * @return The saved user as a ResponseEntity.
//     */
//    @PostMapping("/create")
//    public ResponseEntity<User> createUser(@RequestBody User user) {
//        return ResponseEntity.ok(userService.createUser(user));
//    }

    /**
     * Retrieves the current user from the database.
     * This is used for backend requests to fetch user information.
     *
     * @return The user as a DTO if found, otherwise 404 Not Found.
     */
    @GetMapping("/single")
    public ResponseEntity<UserDTO> getSingleUser() {
        return userService.getSingleUser()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}