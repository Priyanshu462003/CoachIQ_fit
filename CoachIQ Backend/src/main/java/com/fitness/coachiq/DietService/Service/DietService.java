package com.fitness.coachiq.DietService.Service;

import com.fitness.coachiq.DietService.DTO.DietRequest;
import com.fitness.coachiq.DietService.DTO.DietResponse;
import com.fitness.coachiq.DietService.Entity.Diet;
import com.fitness.coachiq.DietService.Entity.DietDay;
import com.fitness.coachiq.DietService.Entity.FoodItem;
import com.fitness.coachiq.DietService.Entity.Meal;
import com.fitness.coachiq.DietService.Repository.DietRepository;
import com.fitness.coachiq.AiService.Service.DietAIService;
import com.fitness.coachiq.AiService.DTO.response.DietDto;
import com.fitness.coachiq.UserService.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.fitness.coachiq.DietService.Entity.Meal;
import com.fitness.coachiq.DietService.Entity.FoodItem;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DietService {

    private final DietRepository dietRepository;
    private final UserService userService;
    private final DietAIService dietAIService;


    // Generate and persist diet in-process.
    public String createDiet(DietRequest request) {
        if (!userService.existByUserId(request.getUserId())) {
            throw new RuntimeException("User not found: " + request.getUserId());
        }

        com.fitness.coachiq.AiService.DTO.DietRequest aiRequest = mapToAIRequest(request);
        DietDto generated = dietAIService.generateDiet(aiRequest);
        saveGeneratedDiet(generated);
        return "Diet generated successfully";
    }

    private com.fitness.coachiq.AiService.DTO.DietRequest mapToAIRequest(DietRequest request) {
        com.fitness.coachiq.AiService.DTO.DietRequest aiRequest =
                new com.fitness.coachiq.AiService.DTO.DietRequest();

        aiRequest.setUserId(request.getUserId());
        aiRequest.setAge(request.getAge());
        aiRequest.setWeight(request.getWeight());
        aiRequest.setHeight(request.getHeight());
        aiRequest.setGender(request.getGender());
        aiRequest.setGoal(request.getGoal());
        aiRequest.setActivityLevel(request.getActivityLevel());
        aiRequest.setAllergies(request.getAllergies());
        aiRequest.setDietaryPreferences(request.getDietaryPreferences());
        aiRequest.setMedicalConditions(request.getMedicalConditions());
        aiRequest.setMealsPerDay(request.getMealsPerDay());
        aiRequest.setAdditionalNotes(request.getAdditionalNotes());

        return aiRequest;
    }

    public DietResponse saveGeneratedDiet(DietDto dto) {

        Diet diet = Diet.builder()
                .userId(dto.getUserId())
                .goal(dto.getGoal())
                .dailyCalories(dto.getDailyCalories())
                .protein(dto.getProtein())
                .carbs(dto.getCarbs())
                .fats(dto.getFats())
                .days(dto.getDays() == null ? List.of() : dto.getDays().stream().map(this::mapToDietDay).toList())
                .recommendations(dto.getRecommendations())
                .build();

        Diet savedDiet = dietRepository.save(diet);

        return mapToResponse(savedDiet);
    }


    private DietDay mapToDietDay(com.fitness.coachiq.AiService.DTO.response.DietDayDto dayDto) {
        return DietDay.builder()
                .dayNumber(dayDto.getDayNumber())
                .meals(dayDto.getMeals() == null ? List.of() : dayDto.getMeals().stream()
                        .map(meal -> Meal.builder()
                                .mealType(meal.getMealType())
                                .time(meal.getTime())
                                .recipe(meal.getRecipe())
                                .foods(meal.getFoods() == null ? List.of() : meal.getFoods().stream()
                                        .map(food -> FoodItem.builder()
                                                .name(food.getName())
                                                .quantity(food.getQuantity())
                                                .build())
                                        .toList())
                                .build())
                        .toList())
                .build();
    }

    private DietResponse mapToResponse(Diet diet) {

        DietResponse response = new DietResponse();

        response.setId(diet.getId());
        response.setUserId(diet.getUserId());
        response.setGoal(diet.getGoal());
        response.setDailyCalories(diet.getDailyCalories());
        response.setProtein(diet.getProtein());
        response.setCarbs(diet.getCarbs());
        response.setFats(diet.getFats());
        response.setDays(diet.getDays());
        response.setRecommendations(diet.getRecommendations());
        response.setCreatedAt(diet.getCreatedAt());
        response.setUpdatedAt(diet.getUpdatedAt());

        return response;
    }


    // Get all diets of a user
    public List<DietResponse> getUserDiets(String userId) {

        List<Diet> diets = dietRepository.findByUserId(userId);

        return diets.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    // Get complete weekly diet
    public DietResponse getDietById(String dietId, String userId) {

        return dietRepository.findById(dietId)
                .filter(diet -> diet.getUserId().equals(userId))
                .map(this::mapToResponse)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Diet not found with id: " + dietId
                        )
                );
    }


    // Get a specific day from the weekly diet
    public DietDay getDietDay(String dietId, Integer dayNumber, String userId) {

        Diet diet = dietRepository.findById(dietId)
                .filter(item -> item.getUserId().equals(userId))
                .orElseThrow(() ->
                        new RuntimeException(
                                "Diet not found with id: " + dietId
                        )
                );

        return diet.getDays()
                .stream()
                .filter(day ->
                        day.getDayNumber().equals(dayNumber)
                )
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException(
                                "Diet day not found: " + dayNumber
                        )
                );
    }
}