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
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutPlanDto {

    private String id;

    private String userId;

    private String goal;

    private String experienceLevel;

    private Integer trainingDaysPerWeek;

    private String overallStrategy;

    // Reusing embedded entity
    private List<WorkoutDay> days;

    private String progressionGuidelines;

    private String recoveryGuidelines;

    private String trainerNotes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}