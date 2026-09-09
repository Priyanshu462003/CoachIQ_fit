package com.fitness.coachiq.AiService.Service;

import com.fitness.coachiq.AiService.DTO.WorkoutPlanRequest;
import com.fitness.coachiq.AiService.DTO.response.WorkoutPlanDto;
import com.fitness.coachiq.AiService.prompt.WorkoutPromptBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkoutAIService {

    private final ChatClient chatClient;
    private final WorkoutPromptBuilder promptBuilder;

    public WorkoutPlanDto generateWorkoutPlan(WorkoutPlanRequest request) {

        log.info("Generating workout plan for user: {}", request.getUserId());

        String prompt = promptBuilder.buildPrompt(request);

        WorkoutPlanDto workoutPlan = chatClient.prompt()
                .user(prompt)
                .call()
                .entity(WorkoutPlanDto.class);

        workoutPlan.setUserId(request.getUserId());
        workoutPlan.setGoal(request.getGoal());
        workoutPlan.setExperienceLevel(request.getExperienceLevel());
        workoutPlan.setTrainingDaysPerWeek(request.getTrainingDaysPerWeek());

        log.info("Workout plan generated successfully for user: {}", request.getUserId());

        return workoutPlan;
    }
}