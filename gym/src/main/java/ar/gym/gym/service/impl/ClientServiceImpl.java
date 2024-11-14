package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.ClientRequestDto;
import ar.gym.gym.dto.request.ClientStatusRequestDto;
import ar.gym.gym.dto.response.ClientResponseDto;
import ar.gym.gym.dto.response.ClientStatusResponseDto;
import ar.gym.gym.dto.response.NotificationResponseDto;
import ar.gym.gym.mapper.ClientMapper;
import ar.gym.gym.mapper.ClientStatusMapper;
import ar.gym.gym.mapper.GymMapper;
import ar.gym.gym.mapper.NotificationMapper;
import ar.gym.gym.model.Client;
import ar.gym.gym.model.ClientStatus;
import ar.gym.gym.model.Gym;
import ar.gym.gym.model.Notification;
import ar.gym.gym.repository.ClientRepository;
import ar.gym.gym.repository.ClientStatusRepository;
import ar.gym.gym.repository.GymRepository;
import ar.gym.gym.service.ClientService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    private final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);
    private final ClientRepository clientRepository;
    private final GymRepository gymRepository;
    private final ClientMapper clientMapper;
    private final GymMapper gymMapper;
    private final ClientStatusRepository clientStatusRepository;
    private final ClientStatusMapper clientStatusMapper;
    private final NotificationMapper notificationMapper;

    public ClientServiceImpl(ClientRepository clientRepository, GymRepository gymRepository, ClientMapper clientMapper, GymMapper gymMapper, ClientStatusRepository clientStatusRepository, ClientStatusMapper clientStatusMapper, NotificationMapper notificationMapper) {
        this.clientRepository = clientRepository;
        this.gymRepository = gymRepository;
        this.clientMapper = clientMapper;
        this.gymMapper = gymMapper;
        this.clientStatusRepository = clientStatusRepository;
        this.clientStatusMapper = clientStatusMapper;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public ClientResponseDto create(ClientRequestDto clientRequestDto) {
        logger.info("Entrando al método create con datos del cliente: {}", clientRequestDto);

        if (clientRepository.findByDni(clientRequestDto.getDni()).isPresent()) {
            throw new EntityExistsException("Ya existe un cliente con el dni " + clientRequestDto.getDni());
        }
        if (clientRepository.findByPhone(clientRequestDto.getPhone()).isPresent()) {
            throw new EntityExistsException("Ya existe un cliente con el número de teléfono " + clientRequestDto.getPhone());
        }
        if (clientRepository.findByEmail(clientRequestDto.getEmail()).isPresent()) {
            throw new EntityExistsException("Ya existe un cliente con el email " + clientRequestDto.getEmail());
        }

        Client client = clientMapper.dtoToEntity(clientRequestDto);
        if (clientRequestDto.getGymName() != null) {
            Optional<Gym> gym = gymRepository.findByName(clientRequestDto.getGymName());

            if (gym.isPresent()) {
                client.setGym(gym.get());
            } else {
                throw new EntityNotFoundException("Gimnasio no encontrado con el nombre: " + clientRequestDto.getGymName());
            }
        }
        client.setActive(true);
        clientRepository.save(client);

        ClientResponseDto response = clientMapper.entityToDto(client);
        logger.info("Saliendo del método create con respuesta: {}", response);
        return response;
    }

    @Override
    public List<ClientResponseDto> findAll() {
        logger.info("Entrando al método findAll");

        List<Client> clients = clientRepository.findAll();
        List<ClientResponseDto> response = clients.stream()
                .map(clientMapper::entityToDto)
                .collect(Collectors.toList());

        logger.info("Saliendo del método findAll con número total de clientes encontrados: {}", response.size());
        return response;
    }

    public Client getClientByDniOrThrow(String dni) {
        logger.info("Entrando al método getClientByDniOrThrow con DNI: {}", dni);

        Client client = clientRepository.findByDni(dni)
                .orElseThrow(() -> new EntityNotFoundException("El cliente con el dni " + dni + " no existe"));

        logger.info("Saliendo del método getClientByDniOrThrow con cliente encontrado: ID={}, Nombre={}",
                client.getId(), client.getName());

        return client;
    }


    @Override
    public ClientResponseDto findByDni(String dni) {
        logger.info("Entrando al método findByDni con DNI: {}", dni);

        Client client = getClientByDniOrThrow(dni);

        ClientResponseDto response = clientMapper.entityToDto(client);
        if (client.getGym()!=null){
            response.setGymName(client.getGym().getName());
        }
        if (client.getNutritionist()!=null){
            response.setNutritionistDni(client.getNutritionist().getDni());
        }
        if (client.getTrainer()!=null){
            response.setTrainerDni(client.getTrainer().getDni());
        }
        logger.info("Saliendo del método findByDni con respuesta: {}", response);
        return response;
    }

    @Override
    public ClientResponseDto findByEmail(String email) {
        logger.info("Entrando al método findByEmail con email: {}", email);

        Optional<Client> client = clientRepository.findByEmail(email);
        if (client.isEmpty()){
            throw new EntityNotFoundException("El cliente con el email " + email + " no existe");
        }
        ClientResponseDto response = clientMapper.entityToDto(client.get());
        if (client.get().getGym()!=null){
            response.setGymName(client.get().getGym().getName());
        }
        if (client.get().getNutritionist()!=null){
            response.setNutritionistDni(client.get().getNutritionist().getDni());
        }
        if (client.get().getTrainer()!=null){
            response.setTrainerDni(client.get().getTrainer().getDni());
        }
        logger.info("Saliendo del método findByEmail con respuesta: {}", response);
        return response;
    }

    @Override
    public ClientResponseDto update(ClientRequestDto clientRequestDto, Long id) {
        logger.info("Entrando al método update con ID: {} y datos de actualización: {}", id, clientRequestDto);

        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con el ID: " + id));

        if (clientRequestDto.getName() != null) {
            existingClient.setName(clientRequestDto.getName());
        }
        if (clientRequestDto.getLastname() != null) {
            existingClient.setLastname(clientRequestDto.getLastname());
        }
        if (clientRequestDto.getDni() != null) {
            existingClient.setDni(clientRequestDto.getDni());
        }
        if (clientRequestDto.getPhone() != null) {
            existingClient.setPhone(clientRequestDto.getPhone());
        }
        if (clientRequestDto.getAddress() != null) {
            existingClient.setAddress(clientRequestDto.getAddress());
        }
        if (clientRequestDto.getEmail() != null) {
            existingClient.setEmail(clientRequestDto.getEmail());
        }
        existingClient.setActive(clientRequestDto.isActive());

        if (clientRequestDto.getGoal() != null) {
            existingClient.setGoal(clientRequestDto.getGoal());
        }

        if (clientRequestDto.getGymName() != null) {
            Optional<Gym> gym = gymRepository.findByName(clientRequestDto.getGymName());

            if (gym.isPresent()) {
                existingClient.setGym(gym.get());
            } else {
                throw new EntityNotFoundException("Gimnasio no encontrado con el nombre: " + clientRequestDto.getGymName());
            }
        }

        if (clientRequestDto.getGymName() != null) {
            Optional<Gym> gym = gymRepository.findByName(clientRequestDto.getGymName());

            if (gym.isPresent()) {
                existingClient.setGym(gym.get());
            } else {
                throw new EntityNotFoundException("Gimnasio no encontrado con el nombre: " + clientRequestDto.getGymName());
            }
        }





        Client updatedClient = clientRepository.save(existingClient);
        ClientResponseDto response = clientMapper.entityToDto(updatedClient);

        logger.info("Saliendo del método update con cliente actualizado: {}", response);
        return response;
    }

    @Override
    public void delete(String id) {
        logger.info("Entrando al método delete con DNI: {}", id);

        Client client = getClientByDniOrThrow(id);
        clientRepository.delete(client);

        logger.info("Cliente eliminado correctamente con DNI: {}", id);
    }

    public ClientResponseDto disableClientByDni(String dni) {
        logger.info("Entrando al método disableClientByDni con DNI: {}", dni);

        Client client = clientRepository.findByDni(dni)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con DNI: " + dni));
        client.setActive(false);
        clientRepository.save(client);

        ClientResponseDto response = clientMapper.entityToDto(client);
        logger.info("Saliendo del método disableClientByDni con cliente desactivado: {}", response);
        return response;
    }

    // Método para obtener los estados de un cliente por DNI
    public List<ClientStatusResponseDto> findClientStatusesByDni(String dni) {
        logger.info("Entrando al método findClientStatusesByDni con DNI: {}", dni);

        Client client = getClientByDniOrThrow(dni);
        List<ClientStatus> statuses = client.getStatuses();

        List<ClientStatusResponseDto> responseDtos = statuses.stream()
                .map(clientStatusMapper::entityToDto)
                .collect(Collectors.toList());

        logger.info("Estados encontrados para el cliente con DNI {}: {}", dni, responseDtos.size());
        return responseDtos;
    }


    // Metodo para agregar un nuevo estado a un cliente por DNI
    public ClientStatusResponseDto addClientStatus(String dni, ClientStatusRequestDto newStatusRequestDto) {
        logger.info("Entrando al método addClientStatus con DNI: {} y estado DTO de solicitud: {}", dni, newStatusRequestDto);

        Optional<Client> clientOptional = clientRepository.findByDni(dni);
        if (clientOptional.isEmpty()) {
            throw new EntityNotFoundException("Cliente no encontrado con el dni: " + dni);
        }

        Client client = clientOptional.get();
        ClientStatus newStatus = clientStatusMapper.dtoToEntity(newStatusRequestDto);
        newStatus.setCreationDate(LocalDate.now());
        clientStatusRepository.save(newStatus);
        client.getStatuses().add(newStatus);
        clientRepository.save(client);

        logger.info("Estado agregado al cliente con DNI: {}", dni);
        return clientStatusMapper.entityToDto(newStatus);
    }
    // Metodo para obtener todas las notificaciones no vistas por el DNI del cliente
    public List<NotificationResponseDto> findByDniAndNotificationsSeenFalse(String dni) {
        // Obtener la lista de notificaciones del repositorio
        List<Notification> notifications = clientRepository.findByDniAndNotificationsSeenFalse(dni);

        // Convertir la lista de Notification a NotificationResponseDto usando el mapper
        return notifications.stream()
                .map(notificationMapper::entityToDto) // Usamos el mapper para convertir cada entidad a su DTO correspondiente
                .collect(Collectors.toList()); // Recoger el resultado en una lista
    }


    @Override
    public NotificationResponseDto markNotificationAsSeen(String dni, Long notificationId) {
        logger.info("Entrando al método markNotificationAsSeen con DNI: {} y Notification ID: {}", dni, notificationId);

        // Buscar el cliente por su DNI
        Client client = getClientByDniOrThrow(dni);

        // Buscar la notificación correspondiente al ID en la lista de notificaciones del cliente
        Notification notification = client.getNotifications().stream()
                .filter(n -> n.getId().equals(notificationId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Notificación no encontrada con ID: " + notificationId));

        // Marcar la notificación como vista
        notification.setSeen(true);

        // Guardar la notificación actualizada en la base de datos
        clientRepository.save(client);  // También guarda el cliente para que se persista la actualización de las notificaciones

        logger.info("Notificación marcada como vista para el cliente con DNI: {}", dni);

        return notificationMapper.entityToDto(notification);  // Devolvemos la notificación actualizada
    }



}
