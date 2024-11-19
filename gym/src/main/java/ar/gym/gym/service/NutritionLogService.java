package ar.gym.gym.service;

import ar.gym.gym.dto.request.MealRequestDto;
import ar.gym.gym.dto.request.NutritionLogRequestDto;
import ar.gym.gym.dto.response.MealResponseDto;
import ar.gym.gym.dto.response.NutritionLogResponseDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NutritionLogService {

    NutritionLogResponseDto createNutritionLog(NutritionLogRequestDto nutritionLogRequestDto);

    List<NutritionLogResponseDto> findAllNutritionLogs();

    Optional<NutritionLogResponseDto> findNutritionLogById(Long id);

    NutritionLogResponseDto updateNutritionLog(Long id, NutritionLogRequestDto nutritionLogRequestDto);

    void deleteNutritionLog(Long id);

    List<NutritionLogResponseDto> findCompletedNutritionLogs();

    List<NutritionLogResponseDto> findNutritionLogsByDate(LocalDateTime date);

    List<NutritionLogResponseDto> findNutritionLogsByClient(Long clientId);

    List<NutritionLogResponseDto> findNutritionLogsByNutritionPlan(Long nutritionPlanId);

    MealResponseDto addMealToNutritionLog(Long nutritionLogId, MealRequestDto mealRequestDto);

    MealResponseDto updateMealInNutritionLog(Long nutritionLogId, Long mealId, MealRequestDto mealRequestDto);

    void deleteMealFromNutritionLog(Long nutritionLogId, Long mealId);

    NutritionLogResponseDto markNutritionLogAsCompleted(Long id);
    NutritionLogResponseDto addObservationToNutritionLog(Long nutritionLogId, String observations);

}
