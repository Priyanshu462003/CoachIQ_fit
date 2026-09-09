package com.fitness.coachiq.Progress.Tracking.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressAnalysisDto {

    private String userId;

    private LocalDate reportMonth;

    private String summary;

    private String strengths;

    private String improvements;

    private String workoutAdvice;

    private String nutritionAdvice;

    private String recoveryAdvice;

    private String motivation;
}