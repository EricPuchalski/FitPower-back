package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.SessionRequestDto;
import ar.gym.gym.dto.response.SessionResponseDto;
import ar.gym.gym.mapper.SessionMapper;
import ar.gym.gym.model.Exercise;
import ar.gym.gym.model.Session;
import ar.gym.gym.repository.SessionRepository;
import ar.gym.gym.service.SessionService;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SessionServiceImpl implements SessionService {
    private SessionRepository sessionRepository;
    private SessionMapper sessionMapper;
    private ExerciseServiceImpl exerciseServiceImpl;

    @Override
    public SessionResponseDto create(SessionRequestDto sessionRequestDto) {
        Session session = sessionMapper.dtoToEntity(sessionRequestDto);
        sessionRepository.save(session);
        return sessionMapper.entityToDto(session);
    }

    @Override
    public List<SessionResponseDto> findAll() {
        List<Session>sessions = sessionRepository.findAll();
        return sessions.stream()
                .map(sessionMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public Session getSessionByCodeOrThrow(Long sessionID) {
        return sessionRepository.findById(sessionID)
                .orElseThrow(() -> new EntityExistsException("La session con el ID " + sessionID + " no existe"));
    }

    @Override
    public SessionResponseDto findById(String id) {
        Session session = getSessionByCodeOrThrow(Long.parseLong(id));
        return sessionMapper.entityToDto(session);
    }

    @Override
    public SessionResponseDto update(SessionRequestDto sessionRequestDto) {


        // Actualizamos solo los campos no nulos o no vacíos del DTO
        if (sessionRequestDto.getSets() > 0) {
            sessionRequestDto.setSets(sessionRequestDto.getSets());
        }
        if (sessionRequestDto.getReps() > 0) {
            sessionRequestDto.setReps(sessionRequestDto.getReps());
        }
        if (sessionRequestDto.getRestTime() != null) {
            sessionRequestDto.setRestTime(sessionRequestDto.getRestTime());
        }
        // Actualizamos el estado de completitud de la sesión
        sessionRequestDto.setCompleted(sessionRequestDto.isCompleted());

        // Guardamos la sesión actualizada en la base de datos
        Session session = sessionMapper.dtoToEntity(sessionRequestDto);
        Session updatedSession = sessionRepository.save(session);

        // Devolvemos el DTO actualizado usando el mapper
        return sessionMapper.entityToDto(updatedSession);
    }

    @Override
    public void delete(Long id) {
        sessionRepository.deleteById(id);
    }

    @Override
    public void addExerciseToSession(Long sessionId, String name) {
        // Buscar la sesión por su ID
        Session session = getSessionByCodeOrThrow(sessionId);

        Optional<Exercise> exercise;

            exercise = exerciseServiceImpl.findByName(name);
        if (exercise.isPresent()){
            // Asignar el ejercicio a la sesión
            session.setExercise(exercise.get());

            // Guardar la sesión actualizada en la base de datos
            Session updatedSession = sessionRepository.save(session);

            // Devolver la sesión actualizada en formato DTO (si es necesario)
            sessionMapper.entityToDto(updatedSession);
        }


    }



}
