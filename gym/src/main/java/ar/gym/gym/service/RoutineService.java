package ar.gym.gym.service;

import ar.gym.gym.dto.request.RoutineRequestDto;
import ar.gym.gym.dto.response.RoutineResponseDto;
import ar.gym.gym.model.Routine;

import java.util.List;

public interface RoutineService {
    RoutineResponseDto create(RoutineRequestDto routineRequestDto);

    List<RoutineResponseDto> findAll();

    Routine getRoutineByCodeOrThrow(Long routineID);

    RoutineResponseDto update(RoutineRequestDto routineRequestDto);

    RoutineResponseDto findById(Long id);

    public void delete(Long id);


}
