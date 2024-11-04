package ar.gym.gym.service;

import ar.gym.gym.dto.request.SessionRequestDto;
import ar.gym.gym.dto.response.SessionResponseDto;
import ar.gym.gym.model.Session;

import java.util.List;

public interface SessionService {
    // Crea una nueva sesión
    public SessionResponseDto create(SessionRequestDto sessionRequestDto);

    // Encuentra todas las sesiones
    public List<SessionResponseDto> findAll();

    // Encuentra una sesión por ID o lanza una excepción si no existe
    public Session getSessionByCodeOrThrow(Long sessionID);

    // Encuentra una sesión por su ID
    public SessionResponseDto findById(String id);

    // Actualiza los datos de una sesión
    public SessionResponseDto update(SessionRequestDto sessionRequestDto);

    // Elimina una sesión por su ID
    void delete(Long id);

    void addExerciseToSession(Long sessionId, String name);

    // Agrega un ejercicio a una sesión
}
