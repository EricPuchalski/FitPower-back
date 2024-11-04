package ar.gym.gym.service;

import ar.gym.gym.dto.request.RoutineRequestDto;
import ar.gym.gym.dto.request.SessionRequestDto;
import ar.gym.gym.dto.response.RoutineResponseDto;
import ar.gym.gym.model.Routine;

import java.util.List;

public interface RoutineService {
    RoutineResponseDto create(RoutineRequestDto routineRequestDto);

    List<RoutineResponseDto> findAll();

    Routine getRoutineByCodeOrThrow(Long routineID);

    RoutineResponseDto update(RoutineRequestDto routineRequestDto, Long id);

    RoutineResponseDto findById(Long id);

    void delete(Long id);

    RoutineResponseDto addSessionToRoutine(Long routineId, SessionRequestDto sessionRequestDto);

    RoutineResponseDto removeSessionFromRoutine(Long routineId, Long sessionId);

    RoutineResponseDto editSessionInRoutine(Long routineId, SessionRequestDto sessionRequestDto);


    }
