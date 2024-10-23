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
    public List<TrainerResponseDto> findAll();

    // Encuentra un entrenador por DNI o lanza una excepción si no existe
    public Trainer getTrainerByDniOrThrow(String dni);

    // Encuentra un entrenador por ID

    public TrainerResponseDto findById(String id);

    // Actualiza los datos de un entrenador

    public TrainerResponseDto update(TrainerRequestDto trainerRequestDto);

    // Elimina un entrenador por su ID

    public void delete(String id);

    // Obtiene la lista de clientes asociados a un entrenador dado su DNI
    public List<ClientResponseDto> getClientsAssociated(String dni);

    // Crea una rutina para un entrenador
    public RoutineResponseDto createRoutine(RoutineRequestDto routineRequestDto);
}
