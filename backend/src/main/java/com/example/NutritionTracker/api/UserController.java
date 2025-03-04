package com.example.NutritionTracker.api;

import com.example.NutritionTracker.dto.UserDTO;
import com.example.NutritionTracker.entity.User;
import com.example.NutritionTracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user") // Singular because there is only one user
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(produces = "application/json")
    public ResponseEntity<UserDTO> getCurrentUser() {
        return userService.getUser()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

}