package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.RoutineRequestDto;
import ar.gym.gym.dto.request.SessionRequestDto;
import ar.gym.gym.dto.response.RoutineResponseDto;
import ar.gym.gym.mapper.RoutineMapper;
import ar.gym.gym.model.Client;
import ar.gym.gym.model.Exercise;
import ar.gym.gym.model.Routine;
import ar.gym.gym.model.Session;
import ar.gym.gym.repository.ClientRepository;
import ar.gym.gym.repository.ExerciseRepository;
import ar.gym.gym.repository.RoutineRepository;
import ar.gym.gym.repository.SessionRepository;
import ar.gym.gym.service.RoutineService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class RoutineServiceImpl implements RoutineService {
    private final RoutineRepository routineRepository;
    private final RoutineMapper routineMapper;
    private final ClientRepository clientRepository;
    private final SessionRepository sessionRepository;

    private final ExerciseRepository exerciseRepository;

    public RoutineServiceImpl(RoutineRepository routineRepository, RoutineMapper routineMapper, ClientRepository clientRepository, SessionRepository sessionRepository, ExerciseRepository exerciseRepository) {
        this.routineRepository = routineRepository;
        this.routineMapper = routineMapper;
        this.clientRepository = clientRepository;
        this.sessionRepository = sessionRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @Override
    public RoutineResponseDto create(RoutineRequestDto routineRequestDto) {
        Client client = clientRepository.findByDni(routineRequestDto.getClientDni())
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        // Convert DTO to entity
        Routine routine = routineMapper.dtoToEntity(routineRequestDto);

        // Set active to false initially
        routine.setActive(false);

        // Set the client to the routine
        routine.setClient(client);

        // Save the new routine
        Routine newRoutine = routineRepository.save(routine);

        // Return the new routine as DTO
        return routineMapper.entityToDto(newRoutine);
    }

    @Override
    public void activateRoutine(String dni, Long routineId) {
        // Fetch the client by ID
        Client client = clientRepository.findByDni(dni)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        // Fetch the routines for the client
        List<Routine> routines = routineRepository.findByClientId(client.getId());

        // Set all routines to inactive
        for (Routine r : routines) {
            r.setActive(false);
            routineRepository.save(r);
        }

        // Activate the selected routine
        Routine routineToActivate = routineRepository.findById(routineId)
                .orElseThrow(() -> new EntityNotFoundException("Routine not found"));

        routineToActivate.setActive(true);
        routineRepository.save(routineToActivate);
    }

    // Method to find the active routine for a specific client
    public RoutineResponseDto getActiveRoutine(String dni) {
        Client client = clientRepository.findByDni(dni)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        // Find the active routine
        Routine activeRoutine = routineRepository.findByClientIdAndActiveTrue(client.getId())
                .orElseThrow(() -> new EntityNotFoundException("Active routine not found"));

        // Return the active routine as DTO
        return routineMapper.entityToDto(activeRoutine);
    }


    @Override
    public RoutineResponseDto getActiveRoutineByEmail(String email) {
        // Buscar el cliente por su email
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        // Buscar la rutina activa del cliente
        Routine activeRoutine = routineRepository.findByClientIdAndActiveTrue(client.getId())
                .orElseThrow(() -> new EntityNotFoundException("Active routine not found"));

        // Convertir la rutina activa a DTO de respuesta y retornarla
        return routineMapper.entityToDto(activeRoutine);
    }




    // Method to find all routines of a client by DNI
    public List<RoutineResponseDto> getRoutinesByClientDni(String clientDni) {
        Client client = clientRepository.findByDni(clientDni)
                .orElseThrow(() -> new EntityNotFoundException("No existe un cliente con el dni: " + clientDni));

        // Fetch all routines for the client
        List<Routine> routines = routineRepository.findByClientId(client.getId());

        // Return the list of routines as DTOs
        return routines.stream()
                .map(routineMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<RoutineResponseDto> getRoutinesByClientEmail(String clientEmail) {
        Client client = clientRepository.findByEmail(clientEmail)
                .orElseThrow(() -> new EntityNotFoundException("No existe un cliente con el email: " + clientEmail));

        // Fetch all routines for the client
        List<Routine> routines = routineRepository.findByClientId(client.getId());

        // Return the list of routines as DTOs
        return routines.stream()
                .map(routineMapper::entityToDto)
                .collect(Collectors.toList());
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
            Optional<Client> client = clientRepository.findByDni(routineRequestDto.getClientDni());
            client.ifPresent(routine::setClient);

        }
        if (routineRequestDto.getName() != null){
            routine.setName(routineRequestDto.getName());
        }

        routine.setCreationDate(LocalDate.from(LocalDateTime.now()));
        routine.setActive(true);
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
    public RoutineResponseDto addSessionToRoutine(Long routineId, SessionRequestDto sessionRequestDto) {
        // Buscar la rutina por su ID
        Routine routine = getRoutineByCodeOrThrow(routineId);

        // Crear la nueva sesión usando el SessionRequestDto
        Session session = new Session();
        session.setSets(sessionRequestDto.getSets());
        session.setReps(sessionRequestDto.getReps());
        session.setRestTime(sessionRequestDto.getRestTime());
        session.setCompleted(sessionRequestDto.isCompleted());

        // Buscar el ejercicio por nombre y asignarlo a la sesión
        Exercise exercise = exerciseRepository.findByName(sessionRequestDto.getExerciseName())
                .orElseThrow(() -> new EntityNotFoundException("El ejercicio con el nombre " + sessionRequestDto.getExerciseName() + " no existe."));
        session.setExercise(exercise);

        // Establecer la rutina en la sesión
        session.setRoutine(routine);

        // Guardar la sesión en la base de datos
        Session savedSession = sessionRepository.save(session);

        // Añadir la sesión guardada a la lista de sesiones de la rutina
        routine.getSessions().add(savedSession);

        // Guardar la rutina actualizada en la base de datos
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
//        sessionServiceImpl.delete(sessionId);

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
//        SessionResponseDto updatedSessionDto = sessionServiceImpl.update(sessionRequestDto);

        // No es necesario manipular la lista de sesiones directamente, ya que se ha actualizado en la base de datos
        // Guardar la rutina actualizada (aunque las sesiones ya están vinculadas)
        Routine updatedRoutine = routineRepository.save(routine);

        // Devolver el DTO de la rutina actualizada usando el mapper
        return routineMapper.entityToDto(updatedRoutine);
    }

}
