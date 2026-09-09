package com.fitness.coachiq.Progress.Tracking.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DailyProgressRequest {

    private String userId;

    private LocalDate date;

    private Double sleepHours;

    private Double proteinIntake;

    private Double waterIntake;

    private Integer caloriesConsumed;

    private Boolean workoutCompleted;

    private Integer cardioMinutes;

    private Integer steps;

    private Integer energyLevel;

    private String mood;

    private String notes;
}