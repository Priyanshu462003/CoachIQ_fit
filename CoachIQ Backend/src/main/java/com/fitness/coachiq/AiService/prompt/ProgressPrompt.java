package com.fitness.coachiq.AiService.prompt;

import com.fitness.coachiq.AiService.DTO.MonthlyProgressRequest;
import com.fitness.coachiq.AiService.DTO.response.AssessmentResponse;
import com.fitness.coachiq.AiService.DTO.response.DailyProgressResponse;
import com.fitness.coachiq.AiService.DTO.response.WeeklyProgressResponse;
import org.springframework.stereotype.Component;

@Component
public class ProgressPrompt {

    public String buildPrompt(MonthlyProgressRequest request) {

        StringBuilder prompt = new StringBuilder();

        prompt.append("""
You are an expert fitness coach and certified nutritionist.

Analyze the user's fitness progress over the last month using ONLY the data provided below.

Your responsibilities:
1. Summarize the user's monthly progress.
2. Identify strengths.
3. Identify areas needing improvement.
4. Give personalized workout advice.
5. Give personalized nutrition advice.
6. Give personalized recovery advice.
7. Motivate the user.
8. Do NOT make assumptions outside the provided data.

Return ONLY valid JSON.

==========================
USER ID:
""");

        prompt.append(request.getUserId()).append("\n\n");

        // ================= DAILY =================

        prompt.append("========== DAILY PROGRESS ==========\n");

        for (DailyProgressResponse day : request.getDailyProgress()) {

            prompt.append("""
Date: %s
Sleep: %.1f hours
Protein: %.1f g
Water: %.1f L
Calories: %d
Workout Completed: %s
Cardio: %d minutes
Steps: %d
Energy: %d/10
Mood: %s
Notes: %s

""".formatted(
                    day.getDate(),
                    day.getSleepHours(),
                    day.getProteinIntake(),
                    day.getWaterIntake(),
                    day.getCaloriesConsumed(),
                    day.getWorkoutCompleted(),
                    day.getCardioMinutes(),
                    day.getSteps(),
                    day.getEnergyLevel(),
                    day.getMood(),
                    day.getNotes()
            ));
        }

        // ================= WEEKLY =================

        prompt.append("\n========== WEEKLY PROGRESS ==========\n");

        for (WeeklyProgressResponse week : request.getWeeklyProgress()) {

            prompt.append("""
Week Start: %s
Weight: %.2f kg
Waist: %.2f cm

""".formatted(
                    week.getWeekStartDate(),
                    week.getWeight(),
                    week.getWaist()
            ));
        }

        // ================= ASSESSMENT =================

        prompt.append("\n========== BODY ASSESSMENTS ==========\n");

        for (AssessmentResponse assessment : request.getAssessments()) {

            prompt.append("""
Assessment Date: %s
Body Fat: %.2f%%
Muscle Mass: %.2f kg
Skeletal Muscle: %.2f%%
Body Water: %.2f%%
Visceral Fat: %.2f
Metabolic Age: %d

""".formatted(
                    assessment.getAssessmentDate(),
                    assessment.getBodyFatPercentage(),
                    assessment.getMuscleMass(),
                    assessment.getSkeletalMuscle(),
                    assessment.getBodyWaterPercentage(),
                    assessment.getVisceralFat(),
                    assessment.getMetabolicAge()
            ));
        }

        // ================= OUTPUT =================

        prompt.append("""

==========================

Return ONLY the following JSON object.

{
  "userId": "<same user id provided>",
  "reportMonth": "2026-07-01",
  "summary": "",
  "strengths": "",
  "improvements": "",
  "workoutAdvice": "",
  "nutritionAdvice": "",
  "recoveryAdvice": "",
  "motivation": ""
}

IMPORTANT RULES:
1. Return ONLY valid JSON.
2. Do NOT use markdown.
3. Do NOT wrap the JSON inside ```json.
4. Do NOT include explanations.
5. reportMonth MUST be in ISO-8601 format (yyyy-MM-dd).
6. Example:
   "reportMonth": "2026-07-01"
7. NEVER return:
   - "July 2026"
   - "July"
   - "07/2026"
8. userId must exactly match the provided USER ID.
9. Every field must be present.
10. All values must be strings.
""");

        return prompt.toString();
    }
}