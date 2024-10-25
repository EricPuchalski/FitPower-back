package ar.gym.gym.service;

import ar.gym.gym.dto.request.RoutineRequestDto;
import ar.gym.gym.dto.request.TrainerRequestDto;
import ar.gym.gym.dto.response.ClientResponseDto;
import ar.gym.gym.dto.response.RoutineResponseDto;
import ar.gym.gym.dto.response.TrainerResponseDto;
import ar.gym.gym.model.Trainer;

import java.util.List;

public interface TrainerService {

    TrainerResponseDto create(TrainerRequestDto trainerRequestDto);
   List<TrainerResponseDto> findAll();

    Trainer getTrainerByDniOrThrow(String dni);

    TrainerResponseDto findByDni(String dni);
    TrainerResponseDto update(TrainerRequestDto trainerRequestDto);

    // Elimina un entrenador por su ID

    void deleteByDni(String dni);

   List<ClientResponseDto> getClientsAssociated(String dni);

    RoutineResponseDto createRoutine(RoutineRequestDto routineRequestDto);
}
