package ar.gym.gym.service;

import ar.gym.gym.dto.request.TrainingPlanRequestDto;
import ar.gym.gym.dto.response.RoutineResponseDto;
import ar.gym.gym.dto.response.TrainingPlanResponseDto;
import ar.gym.gym.model.Client;

import java.util.List;

public interface TrainingPlanService {
    TrainingPlanResponseDto create(TrainingPlanRequestDto trainingPlanRequestDto);

    List<TrainingPlanResponseDto> findAll();

    TrainingPlanResponseDto findByDni(String dni);

    TrainingPlanResponseDto update(TrainingPlanRequestDto trainingPlanRequestDto, Long id);

    void delete(Long id);

    RoutineResponseDto getCurrentActiveRoutine(String clientDni, Long trainingPlanId);

    void completeCurrentRoutine(String clientDni, Long trainingPlanId);

}
