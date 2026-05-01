package com.bhuvi.personalTrackerAPI.controller;

import com.bhuvi.personalTrackerAPI.entity.User;
import com.bhuvi.personalTrackerAPI.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Tag(name = "User API", description = "API for managing user registration and authentication")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/fetch/{userName}")
    @Operation(summary = "Get user by username", description = "Retrieve user details by username")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<?> getUserByUserName(@PathVariable String userName) {
        return userRepository.findByUserName(userName)
                .map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }

}
