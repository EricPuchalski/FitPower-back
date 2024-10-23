package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.SessionRequestDto;
import ar.gym.gym.dto.response.SessionResponseDto;
import ar.gym.gym.mapper.SessionMapper;
import ar.gym.gym.model.Exercise;
import ar.gym.gym.model.Session;
import ar.gym.gym.repository.SessionRepository;
import ar.gym.gym.service.SessionService;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class SessionServiceImpl implements SessionService {
    private SessionRepository sessionRepository;
    private SessionMapper sessionMapper;
    private ExerciseServiceImpl exerciseServiceImpl;

    @Override
    public SessionResponseDto create(SessionRequestDto sessionRequestDto) {
        if (sessionRepository.findById(sessionRequestDto.getId()).isPresent()) {
            throw new EntityExistsException("Ya existe una sesión con el código " + sessionRequestDto.getId());
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
                .orElseThrow(() -> new EntityExistsException("La session con el ID " + sessionID + " no existe"));
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
                .orElseThrow(() -> new EntityExistsException("Sesión no encontrada"));

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
    public void delete(Long id) {
        sessionRepository.deleteById(id);
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
