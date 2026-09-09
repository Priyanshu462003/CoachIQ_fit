package com.fitness.coachiq.AiService.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutPlanDto {

    private String userId;

    private String goal;

    private String experienceLevel;

    private Integer trainingDaysPerWeek;

    private String overallStrategy;

    private List<WorkoutDayDto> days;

    private String progressionGuidelines;

    private String recoveryGuidelines;

    private String trainerNotes;
}