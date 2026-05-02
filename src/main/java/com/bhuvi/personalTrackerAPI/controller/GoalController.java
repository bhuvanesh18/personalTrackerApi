package com.bhuvi.personalTrackerAPI.controller;

import com.bhuvi.personalTrackerAPI.entity.Goal;
import com.bhuvi.personalTrackerAPI.entity.GoalLog;
import com.bhuvi.personalTrackerAPI.repository.GoalLogRepository;
import com.bhuvi.personalTrackerAPI.repository.GoalRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalRepository goalRepository;

    private final GoalLogRepository goalLogRepository;

    @PostMapping("/create")
    @Operation(summary = "Create a new goal", description = "Create a new fitness/health goal for a user")
    @ApiResponse(responseCode = "201", description = "Goal created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public ResponseEntity<?> createGoal(@RequestBody Goal goal) {
        try {
            Goal savedGoal = goalRepository.save(goal);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Goal created successfully");
            response.put("goalId", savedGoal.getGoalId());
            response.put("goalName", savedGoal.getGoalName());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PutMapping("/update/{goalId}")
    @Operation(summary = "Update a goal", description = "Update an existing goal")
    @ApiResponse(responseCode = "200", description = "Goal updated successfully")
    @ApiResponse(responseCode = "404", description = "Goal not found")
    public ResponseEntity<?> updateGoal(@PathVariable Integer goalId, @RequestBody Goal goalDetails) {
        return goalRepository.findById(goalId)
                .map(goal -> {
                    if (goalDetails.getGoalName() != null) goal.setGoalName(goalDetails.getGoalName());
                    if (goalDetails.getGoalDescription() != null) goal.setGoalDescription(goalDetails.getGoalDescription());
                    if (goalDetails.getFrequency() != null) goal.setFrequency(goalDetails.getFrequency());
                    if (goalDetails.getTarget() != null) goal.setTarget(goalDetails.getTarget());
                    if (goalDetails.getUnit() != null) goal.setUnit(goalDetails.getUnit());
                    if (goalDetails.getIsActive() != null) goal.setIsActive(goalDetails.getIsActive());
                    Goal updatedGoal = goalRepository.save(goal);
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Goal updated successfully");
                    response.put("goal", updatedGoal);
                    return ResponseEntity.ok().body(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/fetch/{goalId}")
    @Operation(summary = "Get goal by ID", description = "Retrieve goal details by goal ID")
    @ApiResponse(responseCode = "200", description = "Goal found")
    @ApiResponse(responseCode = "404", description = "Goal not found")
    public ResponseEntity<?> getGoalById(@PathVariable Integer goalId) {
        return goalRepository.findById(goalId)
                .map(goal -> ResponseEntity.ok().body(goal))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{userId}")
    @Operation(summary = "Get all goals for a user", description = "Retrieve all active goals created by a user")
    @ApiResponse(responseCode = "200", description = "Goals retrieved successfully")
    public ResponseEntity<?> getUserGoals(@PathVariable Integer userId) {
        return ResponseEntity.ok(goalRepository.findByCreatedByAndIsActive(userId, 'Y'));
    }

    @PostMapping("/log")
    @Operation(summary = "Create a new goal log", description = "Log a goal completion entry")
    @ApiResponse(responseCode = "201", description = "Goal log created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public ResponseEntity<?> createGoalLog(@RequestBody GoalLog goalLog) {
        try {
            GoalLog savedGoalLog = goalLogRepository.save(goalLog);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Goal log created successfully");
            response.put("logId", savedGoalLog.getLogId());
            response.put("goalId", savedGoalLog.getGoalId());
            response.put("completedOn", savedGoalLog.getCompletedOn());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/logs/{goalId}")
    @Operation(summary = "Get all logs for a goal", description = "Retrieve all completion logs for a specific goal")
    @ApiResponse(responseCode = "200", description = "Goal logs retrieved successfully")
    public ResponseEntity<?> getGoalLogs(@PathVariable Integer goalId) {
        return ResponseEntity.ok(goalLogRepository.findByGoalId(goalId));
    }



}
