package com.fitness.coachiq.AiService.prompt;

import com.fitness.coachiq.AiService.DTO.WorkoutPlanRequest;
import org.springframework.stereotype.Component;

@Component
public class WorkoutPromptBuilder {

    public String buildPrompt(WorkoutPlanRequest request) {

        return String.format("""
You are an elite certified fitness coach.

Generate a COMPLETE personalized workout plan based on the user's information.

IMPORTANT OUTPUT RULES:
- Return ONLY valid JSON.
- Do NOT return markdown.
- Do NOT use ```json.
- Do NOT add explanations before or after the JSON.
- Complete the ENTIRE JSON response.
- Do not stop in the middle of an object or string.
- Keep text fields concise.
- Generate exactly %d training days.
- Generate 4 exercises per training day.

JSON structure:

{
  "overallStrategy": "Short strategy for the complete workout plan",
  "progressionGuidelines": "Short progression strategy",
  "recoveryGuidelines": "Short recovery advice",
  "trainerNotes": "Short trainer notes",
  "days": [
    {
      "dayName": "Monday",
      "focusArea": "Chest + Triceps",
      "dayNotes": "Short note",
      "exercises": [
        {
          "name": "Bench Press",
          "sets": 4,
          "reps": "8-10",
          "restSeconds": 120,
          "intensity": "RPE 8",
          "techniqueTips": "Short technique tip",
          "progressionGuideline": "Short progression",
          "alternativeExercise": "Dumbbell Press"
        }
      ]
    }
  ]
}

USER INFORMATION:

Age: %d
Gender: %s
Weight: %.1f kg
Height: %.1f cm

Goal: %s
Experience Level: %s

Training Days Per Week: %d
Session Duration: %d minutes

Workout Location: %s

Available Equipment:
%s

Injuries:
%s

Medical Conditions:
%s

Focus Areas:
%s

Cardio Preference:
%s

Additional Notes:
%s

RULES:

1. Match the workout to the user's goal.
2. Match exercises to the experience level.
3. Use ONLY available equipment.
4. Do not prescribe exercises that conflict with listed injuries.
5. Use appropriate sets, reps, rest and intensity.
6. Include progressive overload.
7. Include exercise alternatives.
8. Keep exercise descriptions short.
9. Respect the requested training days.
10. Respect the session duration.
11. Do not repeat the same exercise unnecessarily.
12. Return ONLY valid JSON.
""",
                request.getTrainingDaysPerWeek(),
                request.getAge(),
                request.getGender(),
                request.getWeight(),
                request.getHeight(),
                request.getGoal(),
                request.getExperienceLevel(),
                request.getTrainingDaysPerWeek(),
                request.getSessionDurationMinutes(),
                request.getWorkoutLocation(),
                request.getAvailableEquipment(),
                request.getInjuries(),
                request.getMedicalConditions(),
                request.getFocusAreas(),
                request.getCardioPreference(),
                request.getAdditionalNotes()
        );
    }
}