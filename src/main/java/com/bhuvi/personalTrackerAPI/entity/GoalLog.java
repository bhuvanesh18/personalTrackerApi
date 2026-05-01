package com.bhuvi.personalTrackerAPI.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.*;

@Data
@NoArgsConstructor
public class GoalLog {

    private Integer logId;
    private Integer goalId;
    private Integer achieved;
    private LocalDate completedOn;
    private LocalDateTime loggedAt;

    // Custom constructor for creating goal logs
    public GoalLog(Integer goalId, Integer achieved, LocalDate completedOn) {
        this.goalId = goalId;
        this.achieved = achieved;
        this.completedOn = completedOn;
        this.loggedAt = LocalDateTime.now();
    }
}




