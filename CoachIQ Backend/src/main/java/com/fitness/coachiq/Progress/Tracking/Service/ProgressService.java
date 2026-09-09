package com.fitness.coachiq.Progress.Tracking.Service;

import com.fitness.coachiq.Progress.Tracking.DTO.*;
import com.fitness.coachiq.Progress.Tracking.Entity.Assessment;
import com.fitness.coachiq.Progress.Tracking.Entity.DailyProgress;
import com.fitness.coachiq.Progress.Tracking.Entity.ProgressAnalysis;
import com.fitness.coachiq.Progress.Tracking.Entity.WeeklyProgress;
import com.fitness.coachiq.Progress.Tracking.Repository.AssessmentRepository;
import com.fitness.coachiq.Progress.Tracking.Repository.DailyProgressRepository;
import com.fitness.coachiq.Progress.Tracking.Repository.ProgressAnalysisRepository;
import com.fitness.coachiq.Progress.Tracking.Repository.WeeklyProgressRepository;
import com.fitness.coachiq.UserService.Service.UserService;
import com.fitness.coachiq.AiService.Service.ProgressAnalysisService;
import com.fitness.coachiq.AiService.DTO.MonthlyProgressRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgressService {

    private final DailyProgressRepository dailyProgressRepository;

    private final WeeklyProgressRepository weeklyProgressRepository;

    private final AssessmentRepository assessmentRepository;

    private final ProgressAnalysisRepository progressAnalysisRepository;

    private final UserService userService;
    private final ProgressAnalysisService progressAnalysisService;

    private final PdfService pdfService;

    private final EmailService emailService;

    // Same zone the monthly scheduler fires in (app.progress.timezone).
    // LocalDate.now() with no zone uses the JVM's default zone, which is
    // usually UTC on a server/container — not necessarily Asia/Kolkata —
    // so "today" and "last month" could be computed a day off from what
    // the cron actually intends.
    @Value("${app.progress.timezone}")
    private String progressTimezone;


    // Daily Progress
    public DailyProgressResponse saveDailyProgress(DailyProgressRequest request) {
        validateUser(request.getUserId());

        DailyProgress progress = DailyProgress.builder()
                .userId(request.getUserId())
                .date(request.getDate())
                .sleepHours(request.getSleepHours())
                .proteinIntake(request.getProteinIntake())
                .waterIntake(request.getWaterIntake())
                .caloriesConsumed(request.getCaloriesConsumed())
                .workoutCompleted(request.getWorkoutCompleted())
                .cardioMinutes(request.getCardioMinutes())
                .steps(request.getSteps())
                .energyLevel(request.getEnergyLevel())
                .mood(request.getMood())
                .notes(request.getNotes())
                .build();

        DailyProgress savedProgress = dailyProgressRepository.save(progress);

        return DailyProgressResponse.builder()
                .id(savedProgress.getId())
                .userId(savedProgress.getUserId())
                .date(savedProgress.getDate())
                .sleepHours(savedProgress.getSleepHours())
                .proteinIntake(savedProgress.getProteinIntake())
                .waterIntake(savedProgress.getWaterIntake())
                .caloriesConsumed(savedProgress.getCaloriesConsumed())
                .workoutCompleted(savedProgress.getWorkoutCompleted())
                .cardioMinutes(savedProgress.getCardioMinutes())
                .steps(savedProgress.getSteps())
                .energyLevel(savedProgress.getEnergyLevel())
                .mood(savedProgress.getMood())
                .notes(savedProgress.getNotes())
                .createdAt(savedProgress.getCreatedAt())
                .updatedAt(savedProgress.getUpdatedAt())
                .build();
    }

    public List<DailyProgressResponse> getDailyProgress(String userId) {

        validateUser(userId);

        List<DailyProgress> progressList =
                dailyProgressRepository.findByUserIdOrderByDateDesc(userId);

        return progressList.stream()
                .map(progress -> DailyProgressResponse.builder()
                        .id(progress.getId())
                        .userId(progress.getUserId())
                        .date(progress.getDate())
                        .sleepHours(progress.getSleepHours())
                        .proteinIntake(progress.getProteinIntake())
                        .waterIntake(progress.getWaterIntake())
                        .caloriesConsumed(progress.getCaloriesConsumed())
                        .workoutCompleted(progress.getWorkoutCompleted())
                        .cardioMinutes(progress.getCardioMinutes())
                        .steps(progress.getSteps())
                        .energyLevel(progress.getEnergyLevel())
                        .mood(progress.getMood())
                        .notes(progress.getNotes())
                        .createdAt(progress.getCreatedAt())
                        .updatedAt(progress.getUpdatedAt())
                        .build())
                .toList();
    }

    // Weekly Progress
    public WeeklyProgressResponse saveWeeklyProgress(WeeklyProgressRequest request) {
        validateUser(request.getUserId());

        WeeklyProgress weeklyProgress = WeeklyProgress.builder()
                .userId(request.getUserId())
                .weekStartDate(request.getWeekStartDate())
                .weight(request.getWeight())
                .waist(request.getWaist())
                .build();

        WeeklyProgress savedWeeklyProgress = weeklyProgressRepository.save(weeklyProgress);

        return WeeklyProgressResponse.builder()
                .id(savedWeeklyProgress.getId())
                .userId(savedWeeklyProgress.getUserId())
                .weekStartDate(savedWeeklyProgress.getWeekStartDate())
                .weight(savedWeeklyProgress.getWeight())
                .waist(savedWeeklyProgress.getWaist())
                .createdAt(savedWeeklyProgress.getCreatedAt())
                .updatedAt(savedWeeklyProgress.getUpdatedAt())
                .build();
    }

    public List<WeeklyProgressResponse> getWeeklyProgress(String userId) {

        validateUser(userId);

        List<WeeklyProgress> weeklyProgressList =
                weeklyProgressRepository.findByUserIdOrderByWeekStartDateDesc(userId);

        return weeklyProgressList.stream()
                .map(progress -> WeeklyProgressResponse.builder()
                        .id(progress.getId())
                        .userId(progress.getUserId())
                        .weekStartDate(progress.getWeekStartDate())
                        .weight(progress.getWeight())
                        .waist(progress.getWaist())
                        .createdAt(progress.getCreatedAt())
                        .updatedAt(progress.getUpdatedAt())
                        .build())
                .toList();
    }

    // Assessment
    public AssessmentResponse saveAssessment(AssessmentRequest request) {
        validateUser(request.getUserId());

        Assessment assessment = Assessment.builder()
                .userId(request.getUserId())
                .assessmentDate(request.getAssessmentDate())
                .bodyFatPercentage(request.getBodyFatPercentage())
                .muscleMass(request.getMuscleMass())
                .skeletalMuscle(request.getSkeletalMuscle())
                .bodyWaterPercentage(request.getBodyWaterPercentage())
                .visceralFat(request.getVisceralFat())
                .metabolicAge(request.getMetabolicAge())
                .build();

        Assessment savedAssessment = assessmentRepository.save(assessment);

        return AssessmentResponse.builder()
                .id(savedAssessment.getId())
                .userId(savedAssessment.getUserId())
                .assessmentDate(savedAssessment.getAssessmentDate())
                .bodyFatPercentage(savedAssessment.getBodyFatPercentage())
                .muscleMass(savedAssessment.getMuscleMass())
                .skeletalMuscle(savedAssessment.getSkeletalMuscle())
                .bodyWaterPercentage(savedAssessment.getBodyWaterPercentage())
                .visceralFat(savedAssessment.getVisceralFat())
                .metabolicAge(savedAssessment.getMetabolicAge())
                .createdAt(savedAssessment.getCreatedAt())
                .build();
    }

    public List<AssessmentResponse> getAssessments(String userId) {
        validateUser(userId);

        List<Assessment> assessments =
                assessmentRepository.findByUserIdOrderByAssessmentDateDesc(userId);

        return assessments.stream()
                .map(assessment -> AssessmentResponse.builder()
                        .id(assessment.getId())
                        .userId(assessment.getUserId())
                        .assessmentDate(assessment.getAssessmentDate())
                        .bodyFatPercentage(assessment.getBodyFatPercentage())
                        .muscleMass(assessment.getMuscleMass())
                        .skeletalMuscle(assessment.getSkeletalMuscle())
                        .bodyWaterPercentage(assessment.getBodyWaterPercentage())
                        .visceralFat(assessment.getVisceralFat())
                        .metabolicAge(assessment.getMetabolicAge())
                        .createdAt(assessment.getCreatedAt())
                        .build())
                .toList();
    }

    // AI Analysis
    public ProgressAnalysisDto saveProgressAnalysis(ProgressAnalysisDto dto) {


        validateUser(dto.getUserId());

        ProgressAnalysis progressAnalysis = ProgressAnalysis.builder()
                .userId(dto.getUserId())
                .reportMonth(dto.getReportMonth())
                .summary(dto.getSummary())
                .strengths(dto.getStrengths())
                .improvements(dto.getImprovements())
                .workoutAdvice(dto.getWorkoutAdvice())
                .nutritionAdvice(dto.getNutritionAdvice())
                .recoveryAdvice(dto.getRecoveryAdvice())
                .motivation(dto.getMotivation())
                .build();

        ProgressAnalysis savedProgressAnalysis =
                progressAnalysisRepository.save(progressAnalysis);

// Generate PDF
        byte[] pdf = pdfService.generateMonthlyReport(savedProgressAnalysis);

// Fetch user email from User Service
        String email = userService.getUserEmail(savedProgressAnalysis.getUserId());

// Send email
        emailService.sendMonthlyReport(email, pdf);

        return ProgressAnalysisDto.builder()
                .userId(savedProgressAnalysis.getUserId())
                .reportMonth(savedProgressAnalysis.getReportMonth())
                .summary(savedProgressAnalysis.getSummary())
                .strengths(savedProgressAnalysis.getStrengths())
                .improvements(savedProgressAnalysis.getImprovements())
                .workoutAdvice(savedProgressAnalysis.getWorkoutAdvice())
                .nutritionAdvice(savedProgressAnalysis.getNutritionAdvice())
                .recoveryAdvice(savedProgressAnalysis.getRecoveryAdvice())
                .motivation(savedProgressAnalysis.getMotivation())
                .build();

    }

    public List<ProgressAnalysisDto> getProgressAnalysis(String userId) {


        validateUser(userId);

        List<ProgressAnalysis> analyses =
                progressAnalysisRepository.findByUserIdOrderByReportMonthDesc(userId);

        return analyses.stream()
                .map(analysis -> ProgressAnalysisDto.builder()
                        .userId(analysis.getUserId())
                        .reportMonth(analysis.getReportMonth())
                        .summary(analysis.getSummary())
                        .strengths(analysis.getStrengths())
                        .improvements(analysis.getImprovements())
                        .workoutAdvice(analysis.getWorkoutAdvice())
                        .nutritionAdvice(analysis.getNutritionAdvice())
                        .recoveryAdvice(analysis.getRecoveryAdvice())
                        .motivation(analysis.getMotivation())
                        .build())
                .toList();

    }

    public ProgressAnalysisDto getLatestProgressAnalysis(String userId) {


        validateUser(userId);

        ProgressAnalysis analysis = progressAnalysisRepository
                .findFirstByUserIdOrderByReportMonthDesc(userId)
                .orElseThrow(() -> new RuntimeException("No progress analysis found."));

        return ProgressAnalysisDto.builder()
                .userId(analysis.getUserId())
                .reportMonth(analysis.getReportMonth())
                .summary(analysis.getSummary())
                .strengths(analysis.getStrengths())
                .improvements(analysis.getImprovements())
                .workoutAdvice(analysis.getWorkoutAdvice())
                .nutritionAdvice(analysis.getNutritionAdvice())
                .recoveryAdvice(analysis.getRecoveryAdvice())
                .motivation(analysis.getMotivation())
                .build();
    }

    public void generateMonthlyAnalysis() {
        LocalDate endDate = LocalDate.now(ZoneId.of(progressTimezone)).minusDays(1);
        LocalDate startDate = endDate.minusMonths(1);

        for (String userId : userService.getAllUserIds()) {
            List<DailyProgress> dailyProgress = dailyProgressRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
            List<WeeklyProgress> weeklyProgress = weeklyProgressRepository.findByUserIdAndWeekStartDateBetween(userId, startDate, endDate);
            List<Assessment> assessments = assessmentRepository.findByUserIdAndAssessmentDateBetween(userId, startDate, endDate);

            if (dailyProgress.isEmpty() && weeklyProgress.isEmpty() && assessments.isEmpty()) {
                continue;
            }

            MonthlyProgressRequest request = MonthlyProgressRequest.builder()
                    .userId(userId)
                    .dailyProgress(dailyProgress.stream().map(this::mapToDailyResponse).toList())
                    .weeklyProgress(weeklyProgress.stream().map(this::mapToWeeklyResponse).toList())
                    .assessments(assessments.stream().map(this::mapToAssessmentResponse).toList())
                    .build();

            try {
                com.fitness.coachiq.AiService.DTO.response.ProgressAnalysisDto aiAnalysis =
                        progressAnalysisService.generateAnalysis(request);
                // The AI prompt includes a literal example date in its output
                // template ("2026-07-01"), and low-temperature models tend to
                // echo it back verbatim instead of reasoning about the actual
                // report period. Don't trust the model for this field — set
                // it deterministically from the date range we just queried.
                aiAnalysis.setReportMonth(startDate.withDayOfMonth(1));
                saveProgressAnalysis(mapToProgressAnalysisDto(aiAnalysis));
            } catch (Exception e) {
                // One user's AI/report failure must not stop the remaining users.
                org.slf4j.LoggerFactory.getLogger(ProgressService.class).error("Monthly analysis failed for user {}", userId, e);
            }
        }
    }


    private ProgressAnalysisDto mapToProgressAnalysisDto(
            com.fitness.coachiq.AiService.DTO.response.ProgressAnalysisDto aiAnalysis) {

        return ProgressAnalysisDto.builder()
                .userId(aiAnalysis.getUserId())
                .reportMonth(aiAnalysis.getReportMonth())
                .summary(aiAnalysis.getSummary())
                .strengths(aiAnalysis.getStrengths())
                .improvements(aiAnalysis.getImprovements())
                .workoutAdvice(aiAnalysis.getWorkoutAdvice())
                .nutritionAdvice(aiAnalysis.getNutritionAdvice())
                .recoveryAdvice(aiAnalysis.getRecoveryAdvice())
                .motivation(aiAnalysis.getMotivation())
                .build();
    }

    private void validateUser(String userId) {
        if (userId == null || userId.isBlank() || !userService.existByUserId(userId)) {
            throw new RuntimeException("User not found: " + userId);
        }
    }

    private com.fitness.coachiq.AiService.DTO.response.DailyProgressResponse mapToDailyResponse(DailyProgress progress) {

        return com.fitness.coachiq.AiService.DTO.response.DailyProgressResponse.builder()
                .id(progress.getId())
                .userId(progress.getUserId())
                .date(progress.getDate())
                .sleepHours(progress.getSleepHours())
                .proteinIntake(progress.getProteinIntake())
                .waterIntake(progress.getWaterIntake())
                .caloriesConsumed(progress.getCaloriesConsumed())
                .workoutCompleted(progress.getWorkoutCompleted())
                .cardioMinutes(progress.getCardioMinutes())
                .steps(progress.getSteps())
                .energyLevel(progress.getEnergyLevel())
                .mood(progress.getMood())
                .notes(progress.getNotes())
                .createdAt(progress.getCreatedAt())
                .updatedAt(progress.getUpdatedAt())
                .build();
    }
    private com.fitness.coachiq.AiService.DTO.response.WeeklyProgressResponse mapToWeeklyResponse(WeeklyProgress progress) {

        return com.fitness.coachiq.AiService.DTO.response.WeeklyProgressResponse.builder()
                .id(progress.getId())
                .userId(progress.getUserId())
                .weekStartDate(progress.getWeekStartDate())
                .weight(progress.getWeight())
                .waist(progress.getWaist())
                .createdAt(progress.getCreatedAt())
                .updatedAt(progress.getUpdatedAt())
                .build();
    }
    private com.fitness.coachiq.AiService.DTO.response.AssessmentResponse mapToAssessmentResponse(Assessment assessment) {

        return com.fitness.coachiq.AiService.DTO.response.AssessmentResponse.builder()
                .id(assessment.getId())
                .userId(assessment.getUserId())
                .assessmentDate(assessment.getAssessmentDate())
                .bodyFatPercentage(assessment.getBodyFatPercentage())
                .muscleMass(assessment.getMuscleMass())
                .skeletalMuscle(assessment.getSkeletalMuscle())
                .bodyWaterPercentage(assessment.getBodyWaterPercentage())
                .visceralFat(assessment.getVisceralFat())
                .metabolicAge(assessment.getMetabolicAge())
                .createdAt(assessment.getCreatedAt())
                .build();
    }


}