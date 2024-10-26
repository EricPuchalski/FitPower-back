package ar.gym.gym.service;

import ar.gym.gym.dto.request.NutritionistRequestDto;
import ar.gym.gym.dto.response.ClientResponseDto;
import ar.gym.gym.dto.response.NutritionistResponseDto;
import ar.gym.gym.model.Nutritionist;

import java.util.List;

public interface NutritionistService {
    NutritionistResponseDto create(NutritionistRequestDto nutritionistRequestDto);
    List<NutritionistResponseDto> findAll();

    Nutritionist getNutritionistByDniOrThrow(String dni);

    NutritionistResponseDto findByDni(String dni);

    NutritionistResponseDto update(NutritionistRequestDto nutritionistRequestDto);

   void delete(Long id);
    List<ClientResponseDto> getClientsAssociated(String dni);

    NutritionistResponseDto disableNutritionistByDni(String dni);



}
