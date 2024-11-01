package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.RoutineRequestDto;
import ar.gym.gym.dto.request.SessionRequestDto;
import ar.gym.gym.dto.response.RoutineResponseDto;
import ar.gym.gym.dto.response.SessionResponseDto;
import ar.gym.gym.mapper.RoutineMapper;
import ar.gym.gym.model.Client;
import ar.gym.gym.model.Routine;
import ar.gym.gym.model.Session;
import ar.gym.gym.repository.RoutineRepository;
import ar.gym.gym.service.RoutineService;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@AllArgsConstructor
@Service
public class RoutineServiceImpl implements RoutineService {
    private RoutineRepository routineRepository;
    private RoutineMapper routineMapper;
    private SessionServiceImpl sessionServiceImpl;
    private ClientServiceImpl clientServiceImpl;

    @Override
    public RoutineResponseDto create(RoutineRequestDto routineRequestDto) {
        // Verificar si ya existe una rutina con el mismo ID

        // Buscar el cliente por DNI
        Client client = clientServiceImpl.getClientByDniOrThrow(routineRequestDto.getClientDni());

        // Convertimos el request a entidad
        Routine routine = routineMapper.dtoToEntity(routineRequestDto);

        // Establecemos el cliente y el entrenador en la rutina
        routine.setClient(client);
        // Guardamos la rutina en la base de datos
        routineRepository.save(routine);

        // Añadimos la rutina a la lista de rutinas del cliente
        client.getRoutines().add(routine);

        // Devolvemos la rutina creada como DTO
        return routineMapper.entityToDto(routine);
    }


    @Override
    public List<RoutineResponseDto> findAll() {
        List<Routine>routines = routineRepository.findAll();
        return routines.stream()
                .map(routineMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public Routine getRoutineByCodeOrThrow(Long routineID) {
        return routineRepository.findById(routineID)
                .orElseThrow(() -> new EntityExistsException("La rutina con el ID " + routineID + " no existe"));
    }

    @Override
    public RoutineResponseDto findById(Long id) {
        Routine routine = getRoutineByCodeOrThrow(id);
        return routineMapper.entityToDto(routine);
    }

    @Override
    public RoutineResponseDto update(RoutineRequestDto routineRequestDto, Long id) {
        Routine routine = getRoutineByCodeOrThrow(id);

        // Actualizamos el cliente si no está vacío
        if (routineRequestDto.getClientDni() != null) {
            Client client = clientServiceImpl.getClientByDniOrThrow(routineRequestDto.getClientDni());
            routine.setClient(client);
        }
        if (routineRequestDto.getName() != null){
            routine.setName(routineRequestDto.getName());
        }
        // Guardamos la rutina actualizada en la base de datos
        routineRepository.save(routine);

        // Devolvemos el DTO actualizado usando el mapper
        return routineMapper.entityToDto(routine);
    }


    @Override
    public void delete(Long id) {
        Routine routine = getRoutineByCodeOrThrow(id);
        routineRepository.delete(routine);
    }
    @Override
    @Transactional
    public RoutineResponseDto addSessionToRoutine(Long routineId, SessionRequestDto sessionRequestDto, String exerciseName) {
        // Validar entradas
        if (sessionRequestDto == null || exerciseName == null || exerciseName.isEmpty()) {
            throw new IllegalArgumentException("Los datos de la sesión y el nombre del ejercicio son obligatorios.");
        }
        // Buscar la rutina por su ID
        Routine routine = getRoutineByCodeOrThrow(routineId);

        // Crear la nueva sesión
        SessionResponseDto sessionResponseDto = sessionServiceImpl.create(sessionRequestDto);

        // Añadir el ejercicio a la sesión
        sessionServiceImpl.addExerciseToSession(sessionResponseDto.getId(), exerciseName);

        // Buscar la sesión actualizada con el ejercicio asignado
        Session session = sessionServiceImpl.getSessionByCodeOrThrow(sessionResponseDto.getId());

        // Añadir la sesión a la lista de sesiones de la rutina
        routine.getSessions().add(session);

        // Guardar la rutina actualizada
        Routine updatedRoutine = routineRepository.save(routine);

        // Devolver el DTO de la rutina actualizada usando el mapper
        return routineMapper.entityToDto(updatedRoutine);
    }

    @Transactional
    public RoutineResponseDto removeSessionFromRoutine(Long routineId, Long sessionId) {
        // Buscar la rutina por su ID
        Routine routine = getRoutineByCodeOrThrow(routineId);

        // Verificar si la sesión está en la lista
        Session sessionToRemove = routine.getSessions().stream()
                .filter(session -> session.getId().equals(sessionId))
                .findFirst()
                .orElseThrow(() -> new EntityExistsException("La sesión con el ID " + sessionId + " no se encuentra en la rutina con ID " + routineId));

        // Eliminar la sesión de la lista de sesiones de la rutina
        routine.getSessions().remove(sessionToRemove);

        // Llamar al metodo delete del SessionService
        sessionServiceImpl.delete(sessionId);

        // Guardar la rutina actualizada
        Routine updatedRoutine = routineRepository.save(routine);

        // Devolver el DTO de la rutina actualizada usando el mapper
        return routineMapper.entityToDto(updatedRoutine);
    }

    @Transactional
    public RoutineResponseDto editSessionInRoutine(Long routineId, SessionRequestDto sessionRequestDto) {
        // Buscar la rutina por su ID
        Routine routine = getRoutineByCodeOrThrow(routineId);

        // Actualizar la sesión utilizando SessionService
        SessionResponseDto updatedSessionDto = sessionServiceImpl.update(sessionRequestDto);

        // No es necesario manipular la lista de sesiones directamente, ya que se ha actualizado en la base de datos
        // Guardar la rutina actualizada (aunque las sesiones ya están vinculadas)
        Routine updatedRoutine = routineRepository.save(routine);

        // Devolver el DTO de la rutina actualizada usando el mapper
        return routineMapper.entityToDto(updatedRoutine);
    }

}
