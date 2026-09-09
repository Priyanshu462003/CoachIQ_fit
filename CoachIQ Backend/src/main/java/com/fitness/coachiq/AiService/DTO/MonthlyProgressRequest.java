package com.fitness.coachiq.AiService.DTO;

import com.fitness.coachiq.AiService.DTO.response.AssessmentResponse;
import com.fitness.coachiq.AiService.DTO.response.DailyProgressResponse;
import com.fitness.coachiq.AiService.DTO.response.WeeklyProgressResponse;
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