package com.fitness.coachiq.Progress.Tracking.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WeeklyProgressRequest {

    private String userId;

    private LocalDate weekStartDate;

    private Double weight;

    private Double waist;
}