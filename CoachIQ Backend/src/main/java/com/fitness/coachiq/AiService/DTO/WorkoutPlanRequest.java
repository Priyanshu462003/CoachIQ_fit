package com.fitness.coachiq.AiService.DTO;


import lombok.Data;

import java.util.List;

@Data
public class WorkoutPlanRequest {

    private String userId;

    // Basic details
    private Integer age;

    private String gender;

    private Double weight;

    private Double height;

    // Main goal
    // Strength, Hypertrophy, Fat Loss, Endurance, etc.
    private String goal;

    // Beginner, Intermediate, Advanced
    private String experienceLevel;

    // Number of workout days per week
    private Integer trainingDaysPerWeek;

    // Available time per workout session
    private Integer sessionDurationMinutes;

    // Gym, Home, Both
    private String workoutLocation;

    // Example:
    // Dumbbells, Barbell, Bench, Resistance Bands
    private List<String> availableEquipment;

    // Previous injuries
    private List<String> injuries;

    // Health conditions
    private List<String> medicalConditions;

    // Body parts user wants to prioritize
    // Example: Chest, Shoulders, Arms
    private List<String> focusAreas;

    // Cardio preference
    private String cardioPreference;

    // Any extra information
    private String additionalNotes;
}