package com.fitness.coachiq.AiService.DTO;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DietRequest implements Serializable {

    private String userId;

    private Integer age;

    private Double weight;

    private Double height;

    private String gender;

    private String goal;

    private String activityLevel;

    private List<String> allergies;

    private List<String> dietaryPreferences;

    private List<String> medicalConditions;

    private Integer mealsPerDay;

    private String additionalNotes;
}