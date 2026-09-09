package com.fitness.coachiq.WorkoutPlanService.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "workout_plans")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPlan {

    @Id
    private String id;

    // User information
    private String userId;

    // Goal: Strength, Hypertrophy, Fat Loss, etc.
    private String goal;

    // Beginner, Intermediate, Advanced
    private String experienceLevel;

    // Number of training days in a week
    private Integer trainingDaysPerWeek;

    // AI's overall approach for the user
    private String overallStrategy;

    // Weekly workout split followed for 6 months
    private List<WorkoutDay> days;

    // How to apply progressive overload
    private String progressionGuidelines;

    // Sleep, recovery, mobility, rest day advice
    private String recoveryGuidelines;

    // Personal trainer advice and motivation
    private String trainerNotes;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}