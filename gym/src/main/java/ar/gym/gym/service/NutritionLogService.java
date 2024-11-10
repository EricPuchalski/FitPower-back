package ar.gym.gym.service;

import ar.gym.gym.dto.request.NutritionLogRequestDto;
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
}
