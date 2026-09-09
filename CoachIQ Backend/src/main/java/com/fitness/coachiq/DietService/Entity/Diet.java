package com.fitness.coachiq.DietService.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "diets")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Diet {

    @Id
    private String id;

    private String userId;

    private String goal;

    private Integer dailyCalories;

    private Integer protein;

    private Integer carbs;

    private Integer fats;

    private List<DietDay> days;

    private String recommendations;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}