package com.fitness.coachiq.DietService.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Meal {

    private String mealType;

    private String time;

    private List<FoodItem> foods;

    private String recipe;
}