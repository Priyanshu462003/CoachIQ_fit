package com.fitness.coachiq.Progress.Tracking.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "daily_progress")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyProgress {

    @Id
    private String id;

    private String userId;

    private LocalDate date;

    // Recovery
    private Double sleepHours;

    // Nutrition
    private Double proteinIntake;     // grams
    private Double waterIntake;       // liters
    private Integer caloriesConsumed;

    // Activity
    private Boolean workoutCompleted;
    private Integer cardioMinutes;
    private Integer steps;

    // Wellness
    private Integer energyLevel;      // 1-10
    private String mood;

    private String notes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}