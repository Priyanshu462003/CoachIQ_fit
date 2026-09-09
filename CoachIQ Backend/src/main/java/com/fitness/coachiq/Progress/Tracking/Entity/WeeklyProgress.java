package com.fitness.coachiq.Progress.Tracking.Entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "weekly_progress")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyProgress {

    @Id
    private String id;

    private String userId;

    private LocalDate weekStartDate;

    private Double weight;

    private Double waist;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}