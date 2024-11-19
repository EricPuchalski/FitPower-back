package ar.gym.gym.service;

import ar.gym.gym.dto.request.NutritionLogRequestDto;
import ar.gym.gym.dto.request.NutritionPlanRequestDto;
import ar.gym.gym.dto.response.NutritionLogResponseDto;
import ar.gym.gym.dto.response.NutritionPlanResponseDto;

import java.util.List;
import java.util.Optional;

public interface NutritionPlanService {

    NutritionPlanResponseDto createNutritionPlan(NutritionPlanRequestDto nutritionPlanRequestDto);

    List<NutritionPlanResponseDto> findAllNutritionPlans();

    Optional<NutritionPlanResponseDto> findNutritionPlanById(Long id);

    NutritionPlanResponseDto updateNutritionPlan(Long id, NutritionPlanRequestDto nutritionPlanRequestDto);

    void deleteNutritionPlan(Long id);

    List<NutritionPlanResponseDto> findCompletedNutritionPlans();

    List<NutritionPlanResponseDto> findNutritionPlansByClient(Long clientId);

    List<NutritionPlanResponseDto> findNutritionPlansByNutritionist(Long nutritionistId);

    NutritionLogResponseDto addNutritionLogToNutritionPlan(Long nutritionPlanId, NutritionLogRequestDto nutritionLogRequestDto);

    NutritionLogResponseDto updateNutritionLogInNutritionPlan(Long nutritionPlanId, Long nutritionLogId, NutritionLogRequestDto nutritionLogRequestDto);

    void deleteNutritionLogFromNutritionPlan(Long nutritionPlanId, Long nutritionLogId);
}