package com.fitness.coachiq.WorkoutPlanService.Service;

import com.fitness.coachiq.WorkoutPlanService.DTO.WorkoutPlanRequest;
import com.fitness.coachiq.WorkoutPlanService.DTO.WorkoutPlanResponse;
import com.fitness.coachiq.WorkoutPlanService.Entity.WorkoutDay;
import com.fitness.coachiq.WorkoutPlanService.Entity.WorkoutPlan;
import com.fitness.coachiq.WorkoutPlanService.Repository.WorkoutPlanRepository;
import com.fitness.coachiq.AiService.DTO.response.WorkoutPlanDto;
import com.fitness.coachiq.AiService.Service.WorkoutAIService;
import com.fitness.coachiq.UserService.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkoutplanService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final UserService userService;
    private final WorkoutAIService workoutAIService;


    // Generate and persist workout plan in-process.
    public String createWorkoutPlan(WorkoutPlanRequest request) {
        if (!userService.existByUserId(request.getUserId())) {
            throw new RuntimeException("User not found: " + request.getUserId());
        }

        com.fitness.coachiq.AiService.DTO.WorkoutPlanRequest aiRequest = mapToAIRequest(request);
        WorkoutPlanDto generated = workoutAIService.generateWorkoutPlan(aiRequest);
        saveGeneratedWorkoutPlan(generated);
        return "Workout plan generated successfully";
    }


    private com.fitness.coachiq.AiService.DTO.WorkoutPlanRequest mapToAIRequest(WorkoutPlanRequest request) {
        com.fitness.coachiq.AiService.DTO.WorkoutPlanRequest aiRequest =
                new com.fitness.coachiq.AiService.DTO.WorkoutPlanRequest();

        aiRequest.setUserId(request.getUserId());
        aiRequest.setAge(request.getAge());
        aiRequest.setGender(request.getGender());
        aiRequest.setWeight(request.getWeight());
        aiRequest.setHeight(request.getHeight());
        aiRequest.setGoal(request.getGoal());
        aiRequest.setExperienceLevel(request.getExperienceLevel());
        aiRequest.setTrainingDaysPerWeek(request.getTrainingDaysPerWeek());
        aiRequest.setSessionDurationMinutes(request.getSessionDurationMinutes());
        aiRequest.setWorkoutLocation(request.getWorkoutLocation());
        aiRequest.setAvailableEquipment(request.getAvailableEquipment());
        aiRequest.setInjuries(request.getInjuries());
        aiRequest.setMedicalConditions(request.getMedicalConditions());
        aiRequest.setFocusAreas(request.getFocusAreas());
        aiRequest.setCardioPreference(request.getCardioPreference());
        aiRequest.setAdditionalNotes(request.getAdditionalNotes());

        return aiRequest;
    }

    // Save AI-generated workout plan
    public WorkoutPlanResponse saveGeneratedWorkoutPlan(WorkoutPlanDto dto) {

        WorkoutPlan workoutPlan = WorkoutPlan.builder()
                .userId(dto.getUserId())
                .goal(dto.getGoal())
                .experienceLevel(dto.getExperienceLevel())
                .trainingDaysPerWeek(dto.getTrainingDaysPerWeek())
                .overallStrategy(dto.getOverallStrategy())
                .days(dto.getDays() == null ? List.of() : dto.getDays().stream().map(this::mapToWorkoutDay).toList())
                .progressionGuidelines(dto.getProgressionGuidelines())
                .recoveryGuidelines(dto.getRecoveryGuidelines())
                .trainerNotes(dto.getTrainerNotes())
                .build();

        WorkoutPlan savedWorkoutPlan = workoutPlanRepository.save(workoutPlan);

        log.info(
                "Generated workout plan saved successfully for user: {}",
                savedWorkoutPlan.getUserId()
        );

        return mapToResponse(savedWorkoutPlan);
    }

    private WorkoutDay mapToWorkoutDay(com.fitness.coachiq.AiService.DTO.response.WorkoutDayDto dayDto) {
        return WorkoutDay.builder()
                .dayName(dayDto.getDayName())
                .focusArea(dayDto.getFocusArea())
                .dayNotes(dayDto.getDayNotes())
                .exercises(dayDto.getExercises() == null ? List.of() : dayDto.getExercises().stream()
                        .map(exercise -> com.fitness.coachiq.WorkoutPlanService.Entity.Exercise.builder()
                                .name(exercise.getName())
                                .sets(exercise.getSets())
                                .reps(exercise.getReps())
                                .restSeconds(exercise.getRestSeconds())
                                .intensity(exercise.getIntensity())
                                .techniqueTips(exercise.getTechniqueTips())
                                .progressionGuideline(exercise.getProgressionGuideline())
                                .alternativeExercise(exercise.getAlternativeExercise())
                                .build())
                        .toList())
                .build();
    }

    private WorkoutPlanResponse mapToResponse(WorkoutPlan workoutPlan) {

        WorkoutPlanResponse response = new WorkoutPlanResponse();

        response.setId(workoutPlan.getId());
        response.setUserId(workoutPlan.getUserId());
        response.setGoal(workoutPlan.getGoal());
        response.setExperienceLevel(workoutPlan.getExperienceLevel());
        response.setTrainingDaysPerWeek(workoutPlan.getTrainingDaysPerWeek());
        response.setOverallStrategy(workoutPlan.getOverallStrategy());
        response.setDays(workoutPlan.getDays());
        response.setProgressionGuidelines(
                workoutPlan.getProgressionGuidelines()
        );
        response.setRecoveryGuidelines(
                workoutPlan.getRecoveryGuidelines()
        );
        response.setTrainerNotes(workoutPlan.getTrainerNotes());
        response.setCreatedAt(workoutPlan.getCreatedAt());
        response.setUpdatedAt(workoutPlan.getUpdatedAt());

        return response;
    }


    // Get all workout plans of a user
    public List<WorkoutPlanResponse> getUserWorkoutPlans(String userId) {

        List<WorkoutPlan> workoutPlans =
                workoutPlanRepository.findByUserId(userId);

        return workoutPlans.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // Get complete 6-month workout plan
    public WorkoutPlanResponse getWorkoutPlanById(String workoutPlanId, String userId) {

        return workoutPlanRepository.findById(workoutPlanId)
                .filter(plan -> plan.getUserId().equals(userId))
                .map(this::mapToResponse)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Workout plan not found with id: " + workoutPlanId
                        )
                );
    }

    public WorkoutDay getWorkoutPlanDay(
            String workoutPlanId,
            String dayName,
            String userId) {

        WorkoutPlan workoutPlan = workoutPlanRepository.findById(workoutPlanId)
                .filter(plan -> plan.getUserId().equals(userId))
                .orElseThrow(() ->
                        new RuntimeException(
                                "Workout plan not found with id: " + workoutPlanId
                        )
                );

        return workoutPlan.getDays()
                .stream()
                .filter(day ->
                        day.getDayName().equalsIgnoreCase(dayName)
                )
                .findFirst()
                .orElse(
                        WorkoutDay.builder()
                                .dayName(dayName)
                                .exercises(List.of())
                                .build()
                );
    }
}