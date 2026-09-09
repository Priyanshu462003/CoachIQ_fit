package com.fitness.coachiq.Progress.Tracking.Entity;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "progress_analysis")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressAnalysis {

    @Id
    private String id;

    private String userId;
    private LocalDate reportMonth;

    private String summary;

    private String strengths;

    private String improvements;

    private String workoutAdvice;

    private String nutritionAdvice;

    private String recoveryAdvice;

    private String motivation;

    @CreatedDate
    private LocalDateTime createdAt;
}