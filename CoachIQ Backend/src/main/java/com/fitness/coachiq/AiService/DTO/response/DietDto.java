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
public class DietDto {

    private String userId;

    private String goal;

    private Integer dailyCalories;

    private Integer protein;

    private Integer carbs;

    private Integer fats;

    private List<DietDayDto> days;

    private String recommendations;
}