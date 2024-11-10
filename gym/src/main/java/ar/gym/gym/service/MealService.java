package ar.gym.gym.service;

import ar.gym.gym.dto.request.MealRequestDto;
import ar.gym.gym.dto.response.MealResponseDto;

import java.util.List;
import java.util.Optional;

public interface MealService {


    MealResponseDto createMeal(MealRequestDto mealRequestDto);

    MealResponseDto updateMeal(Long id, MealRequestDto mealRequestDto);

    void deleteMeal(Long id);

    List<MealResponseDto> findCompletedMeals();

    List<MealResponseDto> findAllMeals();

    Optional<MealResponseDto> findMealById(Long id);

    MealResponseDto addFoodToMeal(Long mealId, Long foodId);
}
