package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.RoutineRequestDto;
import ar.gym.gym.dto.request.SessionRequestDto;
import ar.gym.gym.dto.response.RoutineResponseDto;
import ar.gym.gym.dto.response.SessionResponseDto;
import ar.gym.gym.mapper.RoutineMapper;
import ar.gym.gym.model.Client;
import ar.gym.gym.model.Routine;
import ar.gym.gym.model.Session;
import ar.gym.gym.model.Trainer;
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
    private TrainerServiceImpl trainerServiceImpl;


    @Override
    public RoutineResponseDto create(RoutineRequestDto routineRequestDto) {
        // Verificar si ya existe una rutina con el mismo ID

        // Buscar el cliente por DNI
        Client client = clientServiceImpl.getClientByDniOrThrow(routineRequestDto.getClientDni());

        // Buscar el entrenador por DNI
        Trainer trainer = trainerServiceImpl.getTrainerByDniOrThrow(routineRequestDto.getTrainerDni());

        // Verificar que el entrenador esté vinculado al cliente
        if (!trainer.getClients().contains(client)) {
            throw new IllegalArgumentException("El entrenador no está vinculado a este cliente.");
        }

        // Convertimos el request a entidad
        Routine routine = routineMapper.dtoToEntity(routineRequestDto);

        // Establecemos el cliente y el entrenador en la rutina
        routine.setClient(client);
        routine.setTrainer(trainer);

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
    public RoutineResponseDto update(RoutineRequestDto routineRequestDto) {
        // Verificamos si la rutina existe por su ID, sino lanzamos excepció

        // Actualizamos solo los campos no nulos o no vacíos del DTO

        // Actualizamos el código de la rutina si no está vacío
        if (routineRequestDto.getRoutineCode() != null && !routineRequestDto.getRoutineCode().isEmpty()) {
            routineRequestDto.setRoutineCode(routineRequestDto.getRoutineCode());
        }

        // Actualizamos el tipo de rutina si no está vacío
        if (routineRequestDto.getRoutineType() != null && !routineRequestDto.getRoutineType().isEmpty()) {
            routineRequestDto.setRoutineType(routineRequestDto.getRoutineType());
        }

        // Actualizamos el entrenador si no está vacío
        if (routineRequestDto.getTrainerDni() != null) {
            Trainer trainer = trainerServiceImpl.getTrainerByDniOrThrow(routineRequestDto.getTrainerDni());
            routineRequestDto.setTrainerDni(trainer.getDni());
        }

        // Actualizamos el cliente si no está vacío
        if (routineRequestDto.getClientDni() != null) {
            Client client = clientServiceImpl.getClientByDniOrThrow(routineRequestDto.getClientDni());
            routineRequestDto.setClientDni(client.getDni());
        }

        // Actualizamos la fecha de creación si está presente
        if (routineRequestDto.getCreationDate() != null) {
            routineRequestDto.setCreationDate(routineRequestDto.getCreationDate());
        }

        // Actualizamos la fecha de inicio si está presente
        if (routineRequestDto.getStartDate() != null) {
            routineRequestDto.setStartDate(routineRequestDto.getStartDate());
        }

        // Actualizamos el estado activo
        routineRequestDto.setActive(routineRequestDto.isActive());

        // Actualizamos el estado de la rutina si está presente
//        if (routineRequestDto.getStatus() != null) {
//            existingRoutine.setS(routineRequestDto.getStatus());
//        }
        Routine routine = routineMapper.dtoToEntity(routineRequestDto);
        // Guardamos la rutina actualizada en la base de datos
        Routine updatedRoutine = routineRepository.save(routine);

        // Devolvemos el DTO actualizado usando el mapper
        return routineMapper.entityToDto(updatedRoutine);
    }


    @Override
    public void delete(Long id) {
        Routine routine = getRoutineByCodeOrThrow(id);
        routineRepository.delete(routine);
    }

    @Transactional
    public RoutineResponseDto addSessionToRoutine(Long routineId, SessionRequestDto sessionRequestDto, Object exerciseIdentifier) {
        // Buscar la rutina por su ID
        Routine routine = getRoutineByCodeOrThrow(routineId);

        // Crear la nueva sesión
        SessionResponseDto sessionResponseDto = sessionServiceImpl.create(sessionRequestDto);

        // Añadir el ejercicio a la sesión
        sessionServiceImpl.addExerciseToSession(sessionResponseDto.getId(), exerciseIdentifier);

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

        // Llamar al método delete del SessionService
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
