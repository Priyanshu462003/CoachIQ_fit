package com.fitness.coachiq.DietService.DTO;

import com.fitness.coachiq.DietService.Entity.DietDay;
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
public class DietResponse {

    private String id;

    private String userId;

    private String goal;

    private Integer dailyCalories;

    private Integer protein;

    private Integer carbs;

    private Integer fats;

    private List<DietDay> days;

    private String recommendations;


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}