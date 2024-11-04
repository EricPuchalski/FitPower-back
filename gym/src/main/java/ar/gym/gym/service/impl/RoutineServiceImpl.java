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
        // Buscar el cliente por DNI
        Optional<Client> client = clientRepository.findByDni(routineRequestDto.getClientDni());

        if (client.isPresent()) {
            // Convertimos el request a entidad
            Routine routine = routineMapper.dtoToEntity(routineRequestDto);

            // Establecer la relación con el cliente
            routine.setClient(client.get());

            routine.setActive(true);
            routine.setCreationDate(LocalDate.now());

            // Añadimos la rutina a la lista de rutinas del cliente
            client.get().getRoutines().add(routine);

            // Guardamos la rutina en la base de datos (esto ahora debería funcionar)
            routineRepository.save(routine);

            // Devolvemos la rutina creada como DTO
            return routineMapper.entityToDto(routine);
        }

        throw new EntityNotFoundException("El cliente con el dni " + routineRequestDto.getClientDni() + " no existe");
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
