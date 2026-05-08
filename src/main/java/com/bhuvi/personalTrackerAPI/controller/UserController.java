package com.bhuvi.personalTrackerAPI.controller;

import com.bhuvi.personalTrackerAPI.entity.User;
import com.bhuvi.personalTrackerAPI.repository.UserRepository;
import com.bhuvi.personalTrackerAPI.service.HashingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "API for managing user registration and authentication")
public class UserController {

    private final UserRepository userRepository;
    private final HashingService hashingService;

    @GetMapping("/fetch/{userName}")
    @Operation(summary = "Get user by username", description = "Retrieve user details by username")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<?> getUserByUserName(@PathVariable String userName) {
        return userRepository.findByUserName(userName)
                .map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset user password", description = "Reset the password for a user")
    @ApiResponse(responseCode = "200", description = "Password reset successful")
    @ApiResponse(responseCode = "404", description = "User not found")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        try {

            if(request.get("mailId") == null || request.get("mailId").equals("")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "mailId should not be empty");
            }

            if(request.get("password") == null || request.get("password").equals("")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password should not be empty");
            }

            User verifiedUser = userRepository.findByVerifiedMailId(request.get("mailId"));
            if (verifiedUser != null) {
                verifiedUser.setPassword(hashingService.hashPassword(request.get("password")));
                userRepository.update(verifiedUser);
                Map<String, String> response = new HashMap<>();
                response.put("message", "Password reset successful");
                return ResponseEntity.ok().body(response);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with mailId: " + request.get("mailId"));
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
