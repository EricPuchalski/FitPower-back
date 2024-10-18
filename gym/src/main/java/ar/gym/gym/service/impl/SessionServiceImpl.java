package ar.gym.gym.service.impl;

import com.itec.FitFlowApp.dto.request.SessionRequestDto;
import com.itec.FitFlowApp.dto.response.SessionResponseDto;
import com.itec.FitFlowApp.exeption.EntityException;
import com.itec.FitFlowApp.mapper.SessionMapper;
import com.itec.FitFlowApp.model.entity.*;
import com.itec.FitFlowApp.model.repository.SessionRepository;
import com.itec.FitFlowApp.util.CRUD;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SessionServiceImpl implements CRUD<SessionResponseDto, SessionRequestDto> {
    private SessionRepository sessionRepository;
    private SessionMapper sessionMapper;
    private ExerciseServiceImpl exerciseServiceImpl;

    @Override
    public SessionResponseDto create(SessionRequestDto sessionRequestDto) {
        if (sessionRepository.findById(sessionRequestDto.getId()).isPresent()) {
            throw new EntityException("Ya existe una sesión con el código " + sessionRequestDto.getId());
        }
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
                .orElseThrow(() -> new EntityException("La session con el ID " + sessionID + " no existe"));
    }

    @Override
    public SessionResponseDto findById(String id) {
        Session session = getSessionByCodeOrThrow(Long.parseLong(id));
        return sessionMapper.entityToDto(session);
    }

    @Override
    public SessionResponseDto update(SessionRequestDto sessionRequestDto) {
        // Verificamos si la sesión existe por su ID, sino lanzamos excepción
        Session existingSession = sessionRepository.findById(sessionRequestDto.getId())
                .orElseThrow(() -> new EntityException("Sesión no encontrada"));

        // Actualizamos solo los campos no nulos o no vacíos del DTO
        if (sessionRequestDto.getTrainingDay() != null && !sessionRequestDto.getTrainingDay().isEmpty()) {
            existingSession.setTrainingDay(sessionRequestDto.getTrainingDay());
        }
        if (sessionRequestDto.getMuscleGroup() != null && !sessionRequestDto.getMuscleGroup().isEmpty()) {
            existingSession.setMuscleGroup(sessionRequestDto.getMuscleGroup());
        }
        if (sessionRequestDto.getSets() > 0) {
            existingSession.setSets(sessionRequestDto.getSets());
        }
        if (sessionRequestDto.getReps() > 0) {
            existingSession.setReps(sessionRequestDto.getReps());
        }
        if (sessionRequestDto.getRestTime() != null) {
            existingSession.setRestTime(sessionRequestDto.getRestTime());
        }
        if (sessionRequestDto.getDuration() != null) {
            existingSession.setDuration(sessionRequestDto.getDuration());
        }

        // Actualizamos el estado de completitud de la sesión
        existingSession.setCompleted(sessionRequestDto.isCompleted());

        // Guardamos la sesión actualizada en la base de datos
        Session updatedSession = sessionRepository.save(existingSession);

        // Devolvemos el DTO actualizado usando el mapper
        return sessionMapper.entityToDto(updatedSession);
    }

    @Override
    public void delete(String id) {
        Session session = getSessionByCodeOrThrow(Long.valueOf(id));
        sessionRepository.delete(session);
    }

    @Transactional
    public void addExerciseToSession(Long sessionId, Object exerciseIdentifier) {
        // Buscar la sesión por su ID
        Session session = getSessionByCodeOrThrow(sessionId);

        Exercise exercise;

        // Determinar si exerciseIdentifier es Long (ID) o String (nombre)
        if (exerciseIdentifier instanceof Long) {
            // Buscar el ejercicio por su ID
            exercise = exerciseServiceImpl.getExerciseByIdOrThrow((Long) exerciseIdentifier);
        } else if (exerciseIdentifier instanceof String) {
            // Buscar el ejercicio por su nombre
            exercise = exerciseServiceImpl.getExerciseByNameOrThrow((String) exerciseIdentifier);
        } else {
            throw new IllegalArgumentException("El identificador del ejercicio debe ser un ID (Long) o un nombre (String)");
        }

        // Asignar el ejercicio a la sesión
        session.setExercise(exercise);

        // Guardar la sesión actualizada en la base de datos
        Session updatedSession = sessionRepository.save(session);

        // Devolver la sesión actualizada en formato DTO (si es necesario)
        sessionMapper.entityToDto(updatedSession);
    }



}
