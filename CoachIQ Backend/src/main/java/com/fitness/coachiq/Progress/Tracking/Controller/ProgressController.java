package com.fitness.coachiq.Progress.Tracking.Controller;

import com.fitness.coachiq.Config.CurrentUser;
import com.fitness.coachiq.Progress.Tracking.DTO.*;
import com.fitness.coachiq.Progress.Tracking.Service.ProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/progress")
@RequiredArgsConstructor
@Tag(name = "Progress", description = "Daily, weekly and assessment progress")
public class ProgressController {
    private final ProgressService progressService;
    private final CurrentUser currentUser;

    @Operation(summary = "Save daily progress")
    @PostMapping("/daily")
    public ResponseEntity<DailyProgressResponse> saveDailyProgress(@Valid @RequestBody DailyProgressRequest request, Authentication authentication) {
        request.setUserId(currentUser.id(authentication));
        return ResponseEntity.ok(progressService.saveDailyProgress(request));
    }

    @Operation(summary = "Get daily progress")
    @GetMapping("/daily")
    public ResponseEntity<List<DailyProgressResponse>> getDailyProgress(Authentication authentication) {
        return ResponseEntity.ok(progressService.getDailyProgress(currentUser.id(authentication)));
    }

    @Operation(summary = "Save weekly progress")
    @PostMapping("/weekly")
    public ResponseEntity<WeeklyProgressResponse> saveWeeklyProgress(@Valid @RequestBody WeeklyProgressRequest request, Authentication authentication) {
        request.setUserId(currentUser.id(authentication));
        return ResponseEntity.ok(progressService.saveWeeklyProgress(request));
    }

    @Operation(summary = "Get weekly progress")
    @GetMapping("/weekly")
    public ResponseEntity<List<WeeklyProgressResponse>> getWeeklyProgress(Authentication authentication) {
        return ResponseEntity.ok(progressService.getWeeklyProgress(currentUser.id(authentication)));
    }

    @Operation(summary = "Save assessment")
    @PostMapping("/assessment")
    public ResponseEntity<AssessmentResponse> saveAssessment(@Valid @RequestBody AssessmentRequest request, Authentication authentication) {
        request.setUserId(currentUser.id(authentication));
        return ResponseEntity.ok(progressService.saveAssessment(request));
    }

    @Operation(summary = "Get assessments")
    @GetMapping("/assessment")
    public ResponseEntity<List<AssessmentResponse>> getAssessments(Authentication authentication) {
        return ResponseEntity.ok(progressService.getAssessments(currentUser.id(authentication)));
    }

    @Operation(summary = "Get progress analysis")
    @GetMapping("/analysis")
    public ResponseEntity<List<ProgressAnalysisDto>> getProgressAnalysis(Authentication authentication) {
        return ResponseEntity.ok(progressService.getProgressAnalysis(currentUser.id(authentication)));
    }

    @Operation(summary = "Get latest progress analysis")
    @GetMapping("/analysis/latest")
    public ResponseEntity<ProgressAnalysisDto> getLatestProgressAnalysis(Authentication authentication) {
        return ResponseEntity.ok(progressService.getLatestProgressAnalysis(currentUser.id(authentication)));
    }
}
