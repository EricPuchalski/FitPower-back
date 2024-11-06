package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.ClientRequestDto;
import ar.gym.gym.dto.response.ClientResponseDto;
import ar.gym.gym.mapper.ClientMapper;
import ar.gym.gym.mapper.GymMapper;
import ar.gym.gym.model.Client;
import ar.gym.gym.model.Gym;
import ar.gym.gym.repository.ClientRepository;
import ar.gym.gym.repository.GymRepository;
import ar.gym.gym.service.ClientService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    public ClientServiceImpl(ClientRepository clientRepository, GymRepository gymRepository, ClientMapper clientMapper, GymMapper gymMapper) {
        this.clientRepository = clientRepository;
        this.gymRepository = gymRepository;
        this.clientMapper = clientMapper;
        this.gymMapper = gymMapper;
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

        logger.info("Saliendo del método getClientByDniOrThrow con cliente encontrado: {}", client);
        return client;
    }

    @Override
    public ClientResponseDto findByDni(String dni) {
        logger.info("Entrando al método findByDni con DNI: {}", dni);

        Client client = getClientByDniOrThrow(dni);
        ClientResponseDto response = clientMapper.entityToDto(client);
        response.setGymName(client.getGym().getName());

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
}
