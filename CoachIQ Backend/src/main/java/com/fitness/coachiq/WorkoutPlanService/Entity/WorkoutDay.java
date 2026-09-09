package com.fitness.coachiq.WorkoutPlanService.Entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutDay {

    // Example: Monday, Tuesday
    private String dayName;

    // Example: Chest + Triceps, Back + Biceps
    private String focusArea;

    // Exercises for this workout day
    private List<Exercise> exercises;

    // AI trainer advice for this day
    private String dayNotes;
}