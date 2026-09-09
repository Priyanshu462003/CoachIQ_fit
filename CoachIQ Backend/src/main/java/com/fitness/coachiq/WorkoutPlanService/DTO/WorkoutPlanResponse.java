package com.fitness.coachiq.WorkoutPlanService.DTO;



import com.fitness.coachiq.WorkoutPlanService.Entity.WorkoutDay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPlanResponse {

    private String id;

    private String userId;

    // User's fitness goal
    private String goal;

    // Beginner, Intermediate, Advanced
    private String experienceLevel;

    // Number of training days per week
    private Integer trainingDaysPerWeek;

    // AI's training strategy
    private String overallStrategy;

    // Personalized workout split
    private List<WorkoutDay> days;

    // Progressive overload instructions
    private String progressionGuidelines;

    // Sleep, rest, mobility, and recovery advice
    private String recoveryGuidelines;

    // Personal trainer notes
    private String trainerNotes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}