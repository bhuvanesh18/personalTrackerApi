package com.bhuvi.personalTrackerAPI.controller;

import com.bhuvi.personalTrackerAPI.entity.OptVerificationRequest;
import com.bhuvi.personalTrackerAPI.entity.User;
import com.bhuvi.personalTrackerAPI.repository.UserRepository;
import com.bhuvi.personalTrackerAPI.service.AuthService;
import com.bhuvi.personalTrackerAPI.service.EmailService;
import com.bhuvi.personalTrackerAPI.service.JwtService;
import com.bhuvi.personalTrackerAPI.service.OptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequiredArgsConstructor
@Tag(name = "Authentication API", description = "API for user authentication and login")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final OptService optService;

    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user credentials and log in")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials")
    public ResponseEntity<?> loginUser(@RequestBody User request) {
        try {
            User authenticatedUser = authService.authenticateUser(request);
            if (authenticatedUser != null) {
                String token = jwtService.generateToken(request.getMailId());

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

            user.setIsActive('N'); // Set user as inactive until OTP verification
            User savedUser = userRepository.save(user);

            final String otp = optService.generateOTP(4);
            System.out.println("Generated a otp");
            System.out.println("Sending OTP to user mail");
            emailService.sendEmail(user.getMailId(), "Personal Tracker: Account Signup Verification", otp);
            System.out.println("Sent OTP to user mail");
            System.out.println("Saving OTP to cache");
            optService.saveOtp(user.getMailId(), otp);
            System.out.println("Saved OTP to cache");

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

    // new post method to verify otp
    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP", description = "Verify the OTP sent to user's email for account activation")
    @ApiResponse(responseCode = "200", description = "OTP verified successfully")
    @ApiResponse(responseCode = "400", description = "Invalid OTP or email")
    public ResponseEntity<?> verifyOtp(@RequestBody OptVerificationRequest request) {
        try {

            if (request.getMailId() == null || request.getMailId().trim().isEmpty()) {
                throw new RuntimeException("Email is required");
            }

            if (request.getOtp() == null || request.getOtp().trim().isEmpty()) {
                throw new RuntimeException("OTP is required");
            }

            boolean isValid = optService.verifyOtp(request.getMailId(), request.getOtp());
            if (isValid) {

                User user = userRepository.findByMailId(request.getMailId());
                user.setIsActive('Y');
                userRepository.update(user);

                String token = jwtService.generateToken(request.getMailId()); // Generate JWT token after successful OTP verification

                Map<String, String> response = new HashMap<>();
                response.put("message", "Verification completed successfully.");
                response.put("token", token);
                response.put("userId", String.valueOf(user.getUserId()));
                response.put("userName", user.getUserName());
                response.put("mailId", user.getMailId());
                response.put("isActive", String.valueOf(user.getIsActive()));
                response.put("createdDate", String.valueOf(user.getCreatedDate()));

                return ResponseEntity.ok().body(response);
            } else {
                throw new RuntimeException("Invalid OTP or email");
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }



    // this is not in use any-more, since JWT token based authorization moved to http header instead of cookie (because cookie generation is blocked by browser when used in incognito)
//    @PostMapping("/logout")
//    @Operation(summary = "User logout", description = "Log out the user by clearing the authentication token")
//    @ApiResponse(responseCode = "200", description = "Logout successful")
//    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
//        ResponseCookie cookie = ResponseCookie.from("AUTH-TOKEN", "")
//                .httpOnly(true)
//                .path("/")
//                .maxAge(0)
//                .secure(true)    // Keep true for production/HTTPS | false for local development (HTTP)
//                .sameSite("None") // Use "None" - if FE and BE are on different domains | "Lax" - for localhost
//                .build();
//
//        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
//
//        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Logout successful"));
//    }


}
