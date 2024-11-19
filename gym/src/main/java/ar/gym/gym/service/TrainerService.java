package ar.gym.gym.service;

import ar.gym.gym.dto.request.TrainerRequestDto;
import ar.gym.gym.dto.request.TrainerUpdateRequestDto;
import ar.gym.gym.dto.response.ClientResponseDto;
import ar.gym.gym.dto.response.TrainerCreateResponseDto;
import ar.gym.gym.dto.response.TrainerResponseDto;
import ar.gym.gym.model.Trainer;

import java.util.List;

public interface TrainerService {

    TrainerCreateResponseDto create(TrainerRequestDto trainerRequestDto);
   List<TrainerResponseDto> findAll();

    Trainer getTrainerByDniOrThrow(String dni);

    TrainerResponseDto findByDni(String dni);
    TrainerResponseDto update(TrainerUpdateRequestDto trainerRequestDto, Long idTrainer);

    // Elimina un entrenador por su ID

    void deleteByDni(String dni);

   List<ClientResponseDto> getClientsAssociated(String dni);

    List<ClientResponseDto> getClientsAssociatedEmail(String email);

}
