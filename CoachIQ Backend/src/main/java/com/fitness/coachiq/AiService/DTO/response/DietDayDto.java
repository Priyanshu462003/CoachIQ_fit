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
public class DietDayDto {

    private Integer dayNumber;

    private List<MealDto> meals;
}