package com.bhuvi.personalTrackerAPI;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import org.springframework.http.ResponseEntity;

@RestController
public class HealthCheckController {

    @GetMapping("/test")
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok().body("API is up!");
    }
}
