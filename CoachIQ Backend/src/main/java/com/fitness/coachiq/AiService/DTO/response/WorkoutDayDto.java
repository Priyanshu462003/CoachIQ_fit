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
public class WorkoutDayDto {

    private String dayName;

    private String focusArea;

    private List<ExerciseDto> exercises;

    private String dayNotes;
}