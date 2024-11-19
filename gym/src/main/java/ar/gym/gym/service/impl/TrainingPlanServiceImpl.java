package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.TrainingPlanRequestDto;
import ar.gym.gym.dto.request.TrainingPlanUpdateRequestDto;
import ar.gym.gym.dto.response.RoutineResponseDto;
import ar.gym.gym.dto.response.TrainingPlanCreateResponseDto;
import ar.gym.gym.dto.response.TrainingPlanResponseDto;
import ar.gym.gym.mapper.RoutineMapper;
import ar.gym.gym.mapper.TrainingPlanMapper;
import ar.gym.gym.model.Client;
import ar.gym.gym.model.Notification;
import ar.gym.gym.model.Routine;
import ar.gym.gym.model.TrainingPlan;
import ar.gym.gym.repository.ClientRepository;
import ar.gym.gym.repository.NotificationRepository;
import ar.gym.gym.repository.RoutineRepository;
import ar.gym.gym.repository.TrainingPlanRepository;
import ar.gym.gym.service.TrainingPlanService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingPlanServiceImpl implements TrainingPlanService {

    private final Logger logger = LoggerFactory.getLogger(TrainingPlanServiceImpl.class);
    private final TrainingPlanRepository trainingPlanRepository;
    private final ClientRepository clientRepository;
    private final TrainingPlanMapper trainingPlanMapper;
    private final RoutineRepository routineRepository;
    private final RoutineMapper routineMapper;
    private final NotificationRepository notificationRepository;

    public TrainingPlanServiceImpl(TrainingPlanRepository trainingPlanRepository, ClientRepository clientRepository, TrainingPlanMapper trainingPlanMapper, RoutineRepository routineRepository, RoutineMapper routineMapper, NotificationRepository notificationRepository) {
        this.trainingPlanRepository = trainingPlanRepository;
        this.clientRepository = clientRepository;
        this.trainingPlanMapper = trainingPlanMapper;
        this.routineRepository = routineRepository;
        this.routineMapper = routineMapper;
        this.notificationRepository = notificationRepository;
    }

    @Override
    public TrainingPlanCreateResponseDto create(TrainingPlanRequestDto trainingPlanRequestDto) {
        logger.info("Entrando al método create con datos del plan de entrenamiento: {}", trainingPlanRequestDto);

        // Obtener cliente por DNI
        Client client = clientRepository.findByDni(trainingPlanRequestDto.getClientDni())
                .orElseThrow(() -> new EntityNotFoundException("El cliente con el DNI " + trainingPlanRequestDto.getClientDni() + " no existe"));

        // Desactivar todos los planes de entrenamiento activos del cliente
        List<TrainingPlan> activePlans = trainingPlanRepository.findByClientAndActiveTrue(client);
        activePlans.forEach(plan -> plan.setActive(false));
        trainingPlanRepository.saveAll(activePlans);

        // Crear y activar el nuevo plan de entrenamiento
        TrainingPlan trainingPlan = trainingPlanMapper.dtoToEntity(trainingPlanRequestDto);
        trainingPlan.setClient(client);
        trainingPlan.setCreationDate(LocalDate.now());
        trainingPlan.setActive(true); // Activar el nuevo plan
        trainingPlanRepository.save(trainingPlan);

        // Notificar al cliente

        Notification notification = Notification.builder().
                creationDate(LocalDateTime.now()).
                seen(false).
                message("Se añadió el plan de entrenamiento:  " + trainingPlan.getName()).
                client(client).build();
        client.getNotifications().add(notification);

        clientRepository.save(client);
        notificationRepository.save(notification);

        TrainingPlanCreateResponseDto response = trainingPlanMapper.entityToDtoCreate(trainingPlan);
        logger.info("Saliendo del método create con respuesta: {}", response);
        return response;
    }
    @Override
    public void setActiveFalseById(Long id) {
        // Buscar el plan de entrenamiento por ID
        TrainingPlan trainingPlan = trainingPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El plan de entrenamiento con ID " + id + " no existe"));

        // Cambiar el estado del plan a inactivo
        trainingPlan.setActive(false);

        // Guardar la entidad con el estado actualizado
        trainingPlanRepository.save(trainingPlan);
    }

    @Override
    public List<TrainingPlanResponseDto> findAll() {
        logger.info("Entrando al método findAll");

        List<TrainingPlan> trainingPlans = trainingPlanRepository.findAll();
        List<TrainingPlanResponseDto> response = trainingPlans.stream()
                .map(trainingPlanMapper::entityToDto)
                .collect(Collectors.toList());

        logger.info("Saliendo del método findAll con número total de planes encontrados: {}", response.size());
        return response;
    }

    public List<RoutineResponseDto> findActiveRoutinesByTrainingPlan(Long trainingPlanId) {
        logger.info("Buscando rutinas activas para el plan de entrenamiento con ID: {}", trainingPlanId);

        // Buscar plan de entrenamiento
        TrainingPlan trainingPlan = trainingPlanRepository.findById(trainingPlanId)
                .orElseThrow(() -> new EntityNotFoundException("El plan de entrenamiento con ID " + trainingPlanId + " no existe"));

        // Obtener las rutinas activas para ese plan
        List<Routine> activeRoutines = routineRepository.findByTrainingPlanAndActiveTrue(trainingPlan);

        // Mapear las rutinas a DTOs
        List<RoutineResponseDto> response = activeRoutines.stream()
                .map(routineMapper::entityToDto)
                .collect(Collectors.toList());

        logger.info("Se encontraron {} rutinas activas para el plan de entrenamiento con ID: {}", response.size(), trainingPlanId);
        return response;
    }

    @Override
    public TrainingPlanResponseDto findById(Long id) {
        logger.info("Entrando al método findById con ID: {}", id);

        TrainingPlan trainingPlan = trainingPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El plan de entrenamiento con el ID " + id + " no existe"));

        TrainingPlanResponseDto response = trainingPlanMapper.entityToDto(trainingPlan);
        logger.info("Saliendo del método findById con respuesta: {}", response);
        return response;
    }

    @Override
    public TrainingPlanResponseDto update(Long id, TrainingPlanUpdateRequestDto trainingPlanRequestDto) {
        logger.info("Entrando al método update con ID: {} y datos de actualización: {}", id, trainingPlanRequestDto);

        TrainingPlan existingTrainingPlan = trainingPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plan de entrenamiento no encontrado con el ID: " + id));

        if (trainingPlanRequestDto.getName() != null) {
            existingTrainingPlan.setName(trainingPlanRequestDto.getName());
        }
        if (trainingPlanRequestDto.getDescription() !=null){
            existingTrainingPlan.setDescription(trainingPlanRequestDto.getDescription());
        }
        // Puedes añadir otros campos de actualización según lo necesites.

        // Si se desea asociar un nuevo cliente, se debe verificar
        if (trainingPlanRequestDto.getClientDni() != null) {
            Client client = clientRepository.findByDni(trainingPlanRequestDto.getClientDni())
                    .orElseThrow(() -> new EntityNotFoundException("El cliente con el DNI " + trainingPlanRequestDto.getClientDni() + " no existe"));
            existingTrainingPlan.setClient(client);
        }

        TrainingPlan updatedTrainingPlan = trainingPlanRepository.save(existingTrainingPlan);
        TrainingPlanResponseDto response = trainingPlanMapper.entityToDto(updatedTrainingPlan);

        logger.info("Saliendo del método update con respuesta: {}", response);
        return response;
    }

    @Override
    public void delete(Long id) {
        logger.info("Entrando al método delete con ID: {}", id);

        TrainingPlan trainingPlan = trainingPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Plan de entrenamiento no encontrado con el ID: " + id));

        trainingPlanRepository.delete(trainingPlan);

        logger.info("Plan de entrenamiento eliminado correctamente con ID: {}", id);
    }

    @Override
    public TrainingPlanResponseDto addRoutineToPlan(Long trainingPlanId, Long routineId) {

        TrainingPlan trainingPlan = trainingPlanRepository.findById(trainingPlanId)
                .orElseThrow(() -> new EntityNotFoundException("El plan de entrenamiento con el ID " + trainingPlanId + " no existe"));

        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new EntityNotFoundException("La rutina con el ID " + routineId + " no existe"));

        logger.info("dato: {}", routine);

        trainingPlan.getRoutines().add(routine);
        trainingPlan = trainingPlanRepository.save(trainingPlan);
        routine.setTrainingPlan(trainingPlan);
        routineRepository.save(routine);

        return trainingPlanMapper.entityToDto(trainingPlan);
    }



    @Override
    @Transactional
    public TrainingPlanResponseDto deactivateTrainingPlan(Long id) {
        logger.info("Entrando al método deactivateTrainingPlan con ID: {}", id);

        // Obtener el plan de entrenamiento desde la base de datos
        TrainingPlan trainingPlan = trainingPlanRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El plan de entrenamiento con el ID " + id + " no existe"));

        // Desactivar el plan de entrenamiento (poner active en false)
        trainingPlan.setActive(false);


        // Guardar el plan de entrenamiento desactivado
        trainingPlan = trainingPlanRepository.save(trainingPlan);

        TrainingPlanResponseDto response = trainingPlanMapper.entityToDto(trainingPlan);
        logger.info("Saliendo del método deactivateTrainingPlan con respuesta: {}", response);
        return response;
    }

    @Override
    public TrainingPlanResponseDto findActiveTrainingPlanByClientDni(String dni) {
        logger.info("Buscando el plan de entrenamiento activo para el cliente con DNI: {}", dni);

        // Buscar cliente por DNI
        Client client = clientRepository.findByDni(dni)
                .orElseThrow(() -> new EntityNotFoundException("El cliente con el DNI " + dni + " no existe"));

        // Obtener la lista de planes activos para el cliente y seleccionar el primero
        TrainingPlan activeTrainingPlan = trainingPlanRepository.findByClientAndActiveTrue(client).stream()
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No se encontró un plan de entrenamiento activo para el cliente con DNI " + dni));

        // Convertir el plan activo a DTO y retornarlo
        TrainingPlanResponseDto response = trainingPlanMapper.entityToDto(activeTrainingPlan);
        logger.info("Plan de entrenamiento activo encontrado para el cliente con DNI {}: {}", dni, response);
        return response;
    }

    @Override
    public TrainingPlanResponseDto getTrainingPlanWithActiveRoutines(String dni) {
        logger.info("Buscando el plan de entrenamiento con solo rutinas activas para el cliente con DNI: {}", dni);

        // Buscar cliente por DNI
        Client client = clientRepository.findByDni(dni)
                .orElseThrow(() -> new EntityNotFoundException("El cliente con el DNI " + dni + " no existe"));

        // Obtener el plan de entrenamiento para el cliente (sin filtrar por activo)
        TrainingPlan trainingPlan = trainingPlanRepository.findByClient(client)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró un plan de entrenamiento para el cliente con DNI " + dni));

        // Filtrar solo las rutinas activas
        List<Routine> activeRoutines = trainingPlan.getRoutines().stream()
                .filter(Routine::isActive)  // Solo rutinas activas
                .toList();

        // Si no hay rutinas activas, lanzamos excepción
        if (activeRoutines.isEmpty()) {
            throw new EntityNotFoundException("El plan de entrenamiento del cliente con DNI " + dni + " no tiene rutinas activas");
        }

        // Convertir las rutinas activas a sus correspondientes DTOs
        List<RoutineResponseDto> activeRoutinesDto = activeRoutines.stream()
                .map(routineMapper::entityToDto) // Convierte cada rutina a DTO
                .collect(Collectors.toList());

        // Convertir el plan de entrenamiento a DTO
        TrainingPlanResponseDto response = trainingPlanMapper.entityToDto(trainingPlan);

        // Establecer solo las rutinas activas en el DTO
        response.setRoutines(activeRoutinesDto);

        logger.info("Plan de entrenamiento con rutinas activas encontrado para el cliente con DNI {}: {}", dni, response);
        return response;
    }
    @Override
    public List<TrainingPlanResponseDto> findAllTrainingPlansByClientDni(String dni) {
        logger.info("Buscando todos los planes de entrenamiento para el cliente con DNI: {}", dni);

        // Obtener todos los planes de entrenamiento
        List<TrainingPlan> allTrainingPlans = trainingPlanRepository.findAll();

        // Filtrar los planes que corresponden al cliente con el DNI dado
        List<TrainingPlan> filteredPlans = allTrainingPlans.stream()
                .filter(trainingPlan -> trainingPlan.getClient() != null && trainingPlan.getClient().getDni().equals(dni))
                .toList();

        // Convertir los planes filtrados a DTOs
        List<TrainingPlanResponseDto> response = filteredPlans.stream()
                .map(trainingPlanMapper::entityToDto)
                .collect(Collectors.toList());

        logger.info("Se encontraron {} planes de entrenamiento para el cliente con DNI: {}", response.size(), dni);
        return response;
    }






}
