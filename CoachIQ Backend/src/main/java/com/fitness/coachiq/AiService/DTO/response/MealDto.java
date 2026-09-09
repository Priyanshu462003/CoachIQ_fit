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
public class MealDto {

    private String mealType;

    private String time;

    private List<FoodItemDto> foods;

    private String recipe;
}