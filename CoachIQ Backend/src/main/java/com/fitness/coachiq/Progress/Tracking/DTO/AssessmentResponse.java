package com.fitness.coachiq.Progress.Tracking.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResponse {

    private String id;

    private String userId;

    private LocalDate assessmentDate;

    private Double bodyFatPercentage;

    private Double muscleMass;

    private Double skeletalMuscle;

    private Double bodyWaterPercentage;

    private Double visceralFat;

    private Integer metabolicAge;

    private LocalDateTime createdAt;
}