package com.bhuvi.personalTrackerAPI.controller;

import com.bhuvi.personalTrackerAPI.entity.User;
import com.bhuvi.personalTrackerAPI.repository.UserRepository;
import com.bhuvi.personalTrackerAPI.service.AuthService;
import com.bhuvi.personalTrackerAPI.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
@Tag(name = "Authentication API", description = "API for user authentication and login")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    JwtService jwtService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user credentials and log in")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials")
    public ResponseEntity<?> loginUser(@RequestBody User request, HttpServletResponse response) {
        try {
            User authenticatedUser = authService.authenticateUser(request);
            if (authenticatedUser != null) {
                String token = jwtService.generateToken(request.getMailId());

                ResponseCookie cookie = ResponseCookie.from("AUTH-TOKEN", token)
                        .httpOnly(true)
                        .secure(true)    // Keep true for production/HTTPS
                        .path("/")
                        .maxAge(10 * 60 * 60)
                        .sameSite("None") // Use "None" if FE and BE are on different domains
                        .build();

                response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

                Map<String, Object> responseBody = new HashMap<>();
                responseBody.put("message", "Login successful");
                responseBody.put("token", token);
                responseBody.put("userId", authenticatedUser.getUserId());
                responseBody.put("userName", authenticatedUser.getUserName());
                responseBody.put("mailId", authenticatedUser.getMailId());
                responseBody.put("isActive", authenticatedUser.getIsActive());
                responseBody.put("createdDate", authenticatedUser.getCreatedDate());

                return ResponseEntity.ok().body(responseBody);
            } else {
                throw new RuntimeException("Invalid credentials");
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PostMapping("/signup")
    @Operation(summary = "Create a new user", description = "Register a new user in the system")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            // Validate input
            if (user.getUserName() == null || user.getUserName().trim().isEmpty()) {
                throw new RuntimeException("Username is required");
            }
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                throw new RuntimeException("Password is required");
            }
            if (user.getMailId() == null || user.getMailId().trim().isEmpty()) {
                throw new RuntimeException("Email is required");
            }

            User savedUser = userRepository.save(user);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User created successfully");
            response.put("mailId", savedUser.getMailId());
            response.put("userName", savedUser.getUserName());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "User logout", description = "Log out the user by clearing the authentication token")
    @ApiResponse(responseCode = "200", description = "Logout successful")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("AUTH-TOKEN", "")
                .httpOnly(true)
                .secure(true)    // Keep true for production/HTTPS
                .path("/")
                .maxAge(10 * 60 * 60)
                .sameSite("None") // Use "None" if FE and BE are on different domains
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Logout successful"));
    }


}
