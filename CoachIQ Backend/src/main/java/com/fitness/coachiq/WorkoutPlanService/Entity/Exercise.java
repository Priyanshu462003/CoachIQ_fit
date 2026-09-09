package com.fitness.coachiq.WorkoutPlanService.Entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Exercise {

    // Example: Bench Press, Squat, Deadlift
    private String name;

    // Example: 3-4 sets
    private Integer sets;

    // Example: 8-12 reps
    private String reps;

    // Rest between sets in seconds
    private Integer restSeconds;

    // Example: RPE 7-8, 70-80% 1RM
    private String intensity;

    // Proper form and execution tips
    private String techniqueTips;

    // Progressive overload instruction
    private String progressionGuideline;

    // Alternative exercise if equipment is unavailable
    private String alternativeExercise;
}