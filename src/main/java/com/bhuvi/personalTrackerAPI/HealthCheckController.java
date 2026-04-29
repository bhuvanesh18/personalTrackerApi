package com.bhuvi.personalTrackerAPI;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@Tag(name = "Health Check", description = "API health check endpoints")
public class HealthCheckController {

    @GetMapping("/test")
    @Operation(summary = "Health check endpoint", description = "Returns a hello-world message to verify the API is running")
    @ApiResponse(responseCode = "200", description = "API is up and running")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok().body("API is up!");
    }
}
