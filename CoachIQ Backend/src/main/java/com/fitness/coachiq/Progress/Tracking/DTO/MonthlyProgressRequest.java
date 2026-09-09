package com.fitness.coachiq.Progress.Tracking.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyProgressRequest {

    private String userId;

    private List<DailyProgressResponse> dailyProgress;

    private List<WeeklyProgressResponse> weeklyProgress;

    private List<AssessmentResponse> assessments;
}