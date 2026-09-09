package com.fitness.coachiq.AiService.prompt;


import com.fitness.coachiq.AiService.DTO.DietRequest;
import org.springframework.stereotype.Component;

@Component
public class DietPromptBuilder {

    public String build(DietRequest request) {

        return String.format("""
You are an expert nutritionist and certified fitness coach.

Your task is to generate a COMPLETE personalized 3-day diet plan.

Return ONLY valid JSON.

Do NOT write markdown.
Do NOT write explanations.
Do NOT wrap JSON inside ```.

The JSON must exactly follow this schema.

{
  "dailyCalories":2200,
  "protein":160,
  "carbs":240,
  "fats":60,

  "recommendations":"General diet recommendations",

  "days":[
    {
      "dayNumber":1,

      "meals":[

        {
          "mealType":"Breakfast",

          "time":"08:00 AM",

          "foods":[

            {
              "name":"Oats",

              "quantity":"80 g"
            }

          ],

          "recipe":"Cook oats with milk."
        }

      ]

    }

  ]

}

Rules

- Generate exactly 3 days.

- Every day should contain exactly %d meals.

- Use realistic  food items.

- Include quantities.

- High protein meals if goal is muscle gain.

- Lower calorie meals if goal is fat loss.

- Respect allergies.

- Respect dietary preferences.

- Respect medical conditions.

- Give practical meals.

- Give different meals every day.

User Information

Age : %d

Gender : %s

Weight : %.1f kg

Height : %.1f cm

Goal : %s

Activity Level : %s

Allergies : %s

Dietary Preferences : %s

Medical Conditions : %s

Additional Notes : %s

Remember:

Return ONLY JSON.

""",

                request.getMealsPerDay(),
                request.getAge(),
                request.getGender(),
                request.getWeight(),
                request.getHeight(),
                request.getGoal(),
                request.getActivityLevel(),
                request.getAllergies(),
                request.getDietaryPreferences(),
                request.getMedicalConditions(),
                request.getAdditionalNotes()

        );
    }

}