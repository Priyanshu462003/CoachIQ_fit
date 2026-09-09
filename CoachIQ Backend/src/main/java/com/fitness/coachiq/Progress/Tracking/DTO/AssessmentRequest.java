package com.fitness.coachiq.Progress.Tracking.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AssessmentRequest {

    private String userId;

    private LocalDate assessmentDate;

    private Double bodyFatPercentage;

    private Double muscleMass;

    private Double skeletalMuscle;

    private Double bodyWaterPercentage;

    private Double visceralFat;

    private Integer metabolicAge;
}