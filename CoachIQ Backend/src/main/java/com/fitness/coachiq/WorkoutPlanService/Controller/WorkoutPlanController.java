package com.fitness.coachiq.WorkoutPlanService.Controller;

import com.fitness.coachiq.Config.CurrentUser;
import com.fitness.coachiq.WorkoutPlanService.DTO.WorkoutPlanRequest;
import com.fitness.coachiq.WorkoutPlanService.DTO.WorkoutPlanResponse;
import com.fitness.coachiq.WorkoutPlanService.Entity.WorkoutDay;
import com.fitness.coachiq.WorkoutPlanService.Service.WorkoutplanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workout-plans")
@RequiredArgsConstructor
@Tag(name = "Workout Plans", description = "AI workout plan generation and retrieval")
public class WorkoutPlanController {
    private final WorkoutplanService workoutPlanService;
    private final CurrentUser currentUser;

    @Operation(summary = "Generate workout plan")
    @PostMapping
    public ResponseEntity<String> createWorkoutPlan(@Valid @RequestBody WorkoutPlanRequest request, Authentication authentication) {
        request.setUserId(currentUser.id(authentication));
        return ResponseEntity.ok(workoutPlanService.createWorkoutPlan(request));
    }

    @Operation(summary = "Get my workout plans")
    @GetMapping
    public ResponseEntity<List<WorkoutPlanResponse>> getUserWorkoutPlans(Authentication authentication) {
        return ResponseEntity.ok(workoutPlanService.getUserWorkoutPlans(currentUser.id(authentication)));
    }

    @Operation(summary = "Get workout plan")
    @GetMapping("/{workoutPlanId}")
    public ResponseEntity<WorkoutPlanResponse> getWorkoutPlanById(@PathVariable String workoutPlanId, Authentication authentication) {
        return ResponseEntity.ok(workoutPlanService.getWorkoutPlanById(workoutPlanId, currentUser.id(authentication)));
    }

    @Operation(summary = "Get workout day")
    @GetMapping("/{workoutPlanId}/days/{dayName}")
    public ResponseEntity<WorkoutDay> getWorkoutPlanDay(@PathVariable String workoutPlanId, @PathVariable String dayName, Authentication authentication) {
        return ResponseEntity.ok(workoutPlanService.getWorkoutPlanDay(workoutPlanId, dayName, currentUser.id(authentication)));
    }
}
