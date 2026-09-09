package com.fitness.coachiq.AiService.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDto {

    private String name;

    private Integer sets;

    private String reps;

    private Integer restSeconds;

    private String intensity;

    private String techniqueTips;

    private String progressionGuideline;

    private String alternativeExercise;
}