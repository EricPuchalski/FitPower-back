package ar.gym.gym.service;

import ar.gym.gym.dto.request.TrainingPlanRequestDto;
import ar.gym.gym.dto.request.TrainingPlanUpdateRequestDto;
import ar.gym.gym.dto.response.RoutineResponseDto;
import ar.gym.gym.dto.response.TrainingPlanCreateResponseDto;
import ar.gym.gym.dto.response.TrainingPlanResponseDto;

import java.util.List;

public interface TrainingPlanService {

    // Crear un nuevo plan de entrenamiento
    TrainingPlanCreateResponseDto create(TrainingPlanRequestDto trainingPlanRequestDto);
    // Obtener todos los planes de entrenamiento
    List<TrainingPlanResponseDto> findAll();

    // Obtener un plan de entrenamiento por ID
    TrainingPlanResponseDto findById(Long id);

    // Actualizar un plan de entrenamiento
    TrainingPlanResponseDto update(Long id, TrainingPlanUpdateRequestDto trainingPlanRequestDto);

    // Eliminar un plan de entrenamiento (probablemente eliminando o desactivando el plan)
    void delete(Long id);

    // Agregar una rutina a un plan de entrenamiento
    TrainingPlanResponseDto addRoutineToPlan(Long trainingPlanId, Long routineId);

    // Desactivar un plan de entrenamiento (poner active en false)
    TrainingPlanResponseDto deactivateTrainingPlan(Long id);
    List<RoutineResponseDto> findActiveRoutinesByTrainingPlan(Long trainingPlanId);

    void setActiveFalseById(Long id);

    TrainingPlanResponseDto findActiveTrainingPlanByClientDni(String dni);

    TrainingPlanResponseDto getTrainingPlanWithActiveRoutines(String dni);

    List<TrainingPlanResponseDto> findAllTrainingPlansByClientDni(String dni);
}
