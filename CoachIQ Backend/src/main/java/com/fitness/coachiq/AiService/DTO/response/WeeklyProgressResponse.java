package com.fitness.coachiq.AiService.DTO.response;

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
public class WeeklyProgressResponse {

    private String id;

    private String userId;

    private LocalDate weekStartDate;

    private Double weight;

    private Double waist;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}