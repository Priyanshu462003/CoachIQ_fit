package com.fitness.coachiq.AiService.DTO.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyProgressResponse {

    private String id;

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

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
