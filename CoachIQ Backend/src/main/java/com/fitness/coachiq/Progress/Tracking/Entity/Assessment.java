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
import java.util.List;

@Document(collection = "assessments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Assessment {

    @Id
    private String id;

    private String userId;

    private LocalDate assessmentDate;

    private Double bodyFatPercentage;

    private Double muscleMass;

    private Double skeletalMuscle;

    private Double bodyWaterPercentage;

    private Double visceralFat;

    private Integer metabolicAge;

    // Optional
    private List<String> progressPhotos;

    @CreatedDate
    private LocalDateTime createdAt;
}