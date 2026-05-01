package com.bhuvi.personalTrackerAPI.entity;

import java.time.LocalDateTime;

import lombok.*;

@Data
@NoArgsConstructor
public class Goal {

    private Integer goalId;
    private String goalName;
    private String goalDescription;
    private Frequency frequency = Frequency.D;
    private Integer target;
    private Unit unit;
    private Integer createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime lud;
    private Character isActive = 'Y';

    // Enums
    public enum Frequency {
        D, W, M, Y
    }

    public enum Unit {
        gram, kilogram, litre, days, weeks, months, metre, kilometre, calories
    }

    // Custom constructor for creating goals
    public Goal(String goalName, String goalDescription, Frequency frequency, Integer target, Unit unit, Integer createdBy) {
        this.goalName = goalName;
        this.goalDescription = goalDescription;
        this.frequency = frequency;
        this.target = target;
        this.unit = unit;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.lud = LocalDateTime.now();
    }
}




