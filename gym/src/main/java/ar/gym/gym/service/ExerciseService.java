package ar.gym.gym.service;

import ar.gym.gym.dto.request.ClientRequestDto;
import ar.gym.gym.dto.request.ExerciseRequestDto;
import ar.gym.gym.dto.response.ClientResponseDto;
import ar.gym.gym.dto.response.ExerciseResponseDto;

import java.util.List;
import java.util.Optional;

public interface ExerciseService {

    ExerciseResponseDto create(ExerciseRequestDto exerciseRequestDto);

    List<ExerciseResponseDto> findAll();

    ExerciseResponseDto findById(Long id);

    ExerciseResponseDto update(ExerciseRequestDto exerciseRequestDto);

    void delete(Long id);

    Optional<ExerciseResponseDto> findByName(String name);
}
