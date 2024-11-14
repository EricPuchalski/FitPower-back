package ar.gym.gym.service;

import ar.gym.gym.dto.request.ExerciseRequestDto;
import ar.gym.gym.dto.response.ExerciseResponseDto;
import ar.gym.gym.model.Exercise;

import java.util.List;
import java.util.Optional;

public interface ExerciseService {

    ExerciseResponseDto create(ExerciseRequestDto exerciseRequestDto);

    List<ExerciseResponseDto> findAll();

    ExerciseResponseDto findById(Long id);

    ExerciseResponseDto update(ExerciseRequestDto exerciseRequestDto, Long id);

    void delete(Long id);

    Optional<Exercise> findByName(String name);
}
