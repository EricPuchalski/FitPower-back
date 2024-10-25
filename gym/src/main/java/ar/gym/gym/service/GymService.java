package ar.gym.gym.service;


import ar.gym.gym.dto.request.GymRequestDto;
import ar.gym.gym.dto.response.AddClientToNutritionistResponseDto;
import ar.gym.gym.dto.response.GymResponseDto;
import ar.gym.gym.model.Gym;

import java.util.List;
import java.util.Optional;

public interface GymService {
    GymResponseDto create(GymRequestDto gymRequestDto);
    public List<GymResponseDto> findAll();
    public Optional<Gym> findByName(String name);
    Gym getGymByCodeOrThrow(String gymCode);
    public GymResponseDto update(GymRequestDto gymRequestDto, Long id);
    public void deleteByGymCode(String gymCode);

    GymResponseDto addClientToGym(String gymCode, String dni);

    GymResponseDto addTrainerToGym(String gymCode, String dni);

    GymResponseDto addNutritionistToGym(String gymCode, String dni);

    void assignTrainerToClient(String dniTrainer, String dniClient);

    AddClientToNutritionistResponseDto assignNutritionistToClient(String dniNutritionist, String dniClient);


}
