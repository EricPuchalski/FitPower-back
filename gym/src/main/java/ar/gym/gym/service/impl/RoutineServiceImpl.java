package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.RoutineRequestDto;
import ar.gym.gym.dto.request.SessionRequestDto;
import ar.gym.gym.dto.response.RoutineResponseDto;
import ar.gym.gym.mapper.RoutineMapper;
import ar.gym.gym.model.*;
import ar.gym.gym.repository.*;
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
    private final NotificationRepository notificationRepository;

    private final TrainingPlanRepository trainingPlanRepository;

    public RoutineServiceImpl(RoutineRepository routineRepository, RoutineMapper routineMapper, ClientRepository clientRepository, SessionRepository sessionRepository, ExerciseRepository exerciseRepository, NotificationRepository notificationRepository, TrainingPlanRepository trainingPlanRepository) {
        this.routineRepository = routineRepository;
        this.routineMapper = routineMapper;
        this.clientRepository = clientRepository;
        this.sessionRepository = sessionRepository;
        this.exerciseRepository = exerciseRepository;
        this.notificationRepository = notificationRepository;
        this.trainingPlanRepository = trainingPlanRepository;
    }

    @Override
    public RoutineResponseDto create(RoutineRequestDto routineRequestDto) {
        Client client = clientRepository.findByDni(routineRequestDto.getClientDni())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        // Convert DTO to entity
        Routine routine = routineMapper.dtoToEntity(routineRequestDto);

        // Set active to false initially
        routine.setActive(true);
        routine.setCompleted(false);

        // Set the client to the routine
        routine.setClient(client);

        routine.setCreationDate(LocalDate.now());

        // Save the new routine
        Routine newRoutine = routineRepository.save(routine);

        //Notificar al cliente
        Notification notification = Notification.builder().
                creationDate(LocalDateTime.now()).
                seen(false).
                message("Se añadió la rutina " + routineRequestDto.getName()).
                client(client).build();
        client.getNotifications().add(notification);

        clientRepository.save(client);
        notificationRepository.save(notification);

        // Return the new routine as DTO
        return routineMapper.entityToDto(newRoutine);
    }

    @Override
    public void activateRoutine(String dni, Long routineId) {
        // Fetch the client by ID
        Client client = clientRepository.findByDni(dni)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

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

    @Override
    public RoutineResponseDto completeRoutine(Long idRoutine) {
        // 1. Buscar la rutina por su ID
        Routine routine = routineRepository.findById(idRoutine)
                .orElseThrow(() -> new EntityNotFoundException("Routine not found"));

        // 2. Marcar la rutina como completada, pero no guardarla aún
        routine.setCompleted(true);

        // 3. Obtener el DNI del cliente asociado a la rutina
        String clientDni = routine.getClient().getDni();

        // 4. Obtener el cliente asociado a la rutina
        Client client = clientRepository.findByDni(clientDni)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        // 5. Obtener el plan de entrenamiento activo del cliente
        List<TrainingPlan> activeTrainingPlans = trainingPlanRepository.findByClientAndActiveTrue(client);
        if (activeTrainingPlans.isEmpty()) {
            throw new EntityNotFoundException("Active training plan not found");
        }
        TrainingPlan activeTrainingPlan = activeTrainingPlans.get(0);

        // 6. Obtener todas las rutinas activas del plan de entrenamiento activo
        List<Routine> activeRoutines = activeTrainingPlan.getRoutines().stream()
                .filter(Routine::isActive)
                .collect(Collectors.toList());

        // 7. Verificar si todas las rutinas activas están completadas
        boolean allCompleted = activeRoutines.stream().allMatch(Routine::isCompleted);

        if (allCompleted) {
            // 8. Si todas están completadas, restablecer el estado de todas las rutinas activas a no completado
            activeRoutines.forEach(r -> r.setCompleted(false));
            routineRepository.saveAll(activeRoutines);
        } else {
            // Si no todas están completadas, guardar la rutina actual individualmente
            routineRepository.save(routine);
        }

        // 9. Convertir la rutina actualizada a un DTO y devolverla
        return routineMapper.entityToDto(routine);
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

        // Fetch all routines for the client and filter by active status
        List<Routine> routines = routineRepository.findByClientId(client.getId())
                .stream()
                .filter(Routine::isActive)  // Filtra solo las rutinas activas
                .toList();

        // Convert the list of active routines to DTOs
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
        //Notificar al cliente
        Notification notification = Notification.builder().
                creationDate(LocalDateTime.now()).
                seen(false).
                message("Se añadió una sesión a la rutina " + routine.getName()).build();
        routine.getClient().getNotifications().add(notification);
        notificationRepository.save(notification);

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
        //Notificar al cliente
        Notification notification = Notification.builder().
                creationDate(LocalDateTime.now()).
                seen(false).
                message("Se ha removido una sesión en la rutina " + routine.getName()).build();
        routine.getClient().getNotifications().add(notification);
        notificationRepository.save(notification);

        // Devolver el DTO de la rutina actualizada usando el mapper
        return routineMapper.entityToDto(updatedRoutine);
    }

    @Transactional
    @Override
    public RoutineResponseDto editSessionInRoutine(Long routineId, Long sessionId, SessionRequestDto sessionRequestDto) {
        // Buscar la rutina por su ID
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new EntityExistsException("Routine not found"));

        // Buscar la sesión correspondiente a editar utilizando el sessionId
        Session sessionToUpdate = routine.getSessions().stream()
                .filter(session -> session.getId().equals(sessionId))
                .findFirst()
                .orElseThrow(() -> new EntityExistsException("Session not found"));

        // Actualizar la sesión con los datos del DTO
        sessionToUpdate.setSets(sessionRequestDto.getSets());
        sessionToUpdate.setReps(sessionRequestDto.getReps());
        sessionToUpdate.setRestTime(sessionRequestDto.getRestTime());
        if (sessionRequestDto.getExerciseName()!=null){
            Optional<Exercise> exercise = exerciseRepository.findByName(sessionRequestDto.getExerciseName());
            if (exercise.isEmpty()){
                throw new EntityNotFoundException("Exercise not found");
            }
            sessionToUpdate.setExercise(exercise.get());

        }

        // Aquí puedes agregar más campos si es necesario

        // Si la relación entre Routine y Session tiene cascade, no es necesario guardar la sesión
        routineRepository.save(routine); // Esto guarda la rutina y la sesión si la relación es cascada

        //Notificar al cliente
        Notification notification = Notification.builder().
                creationDate(LocalDateTime.now()).
                seen(false).
                message("Se ha editado una sesión en la rutina " + routine.getName()).build();
        routine.getClient().getNotifications().add(notification);
        notificationRepository.save(notification);

        // Devolver el DTO de la rutina actualizada
        return routineMapper.entityToDto(routine);
    }

    @Transactional
    @Override
    public RoutineResponseDto deactivateRoutine(Long routineId) {
        // Buscar la rutina por su ID
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new EntityNotFoundException("Routine not found"));

        // Desactivar la rutina (setActive false)
        routine.setActive(false);

        // Guardar la rutina desactivada en la base de datos
        routineRepository.save(routine);

        // Convertir la rutina desactivada a DTO y devolverla
        return routineMapper.entityToDto(routine);
    }



}
