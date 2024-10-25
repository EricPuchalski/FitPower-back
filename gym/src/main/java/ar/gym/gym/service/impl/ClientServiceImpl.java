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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {
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
        if(clientRepository.findByDni(clientRequestDto.getDni()).isPresent()){
            throw new EntityExistsException("Ya existe un cliente con el código " + clientRequestDto.getDni());
        }
        Client client = clientMapper.dtoToEntity(clientRequestDto);
        Optional<Gym> gym = gymRepository.findByName(clientRequestDto.getGymName());
        if (gym.isPresent()){
            client.setGym(gym.get());
        } else {
            throw new EntityNotFoundException("No se encontró el gimnasio con el nombre " + clientRequestDto.getGymName());
        }
        client.setActive(true);
        clientRepository.save(client);
        return clientMapper.entityToDto(client);
    }

    @Override
    public List<ClientResponseDto> findAll() {
        List<Client>clients = clientRepository.findAll();
        return clients.stream()
                .map(clientMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public Client getClientByDniOrThrow(String dni){
        return clientRepository.findByDni(dni)
                .orElseThrow(() -> new EntityExistsException("El cliente con el dni " + dni + " no existe"));
    }
    @Override
    public ClientResponseDto findByDni(String dni) {
        Client client = getClientByDniOrThrow(dni);
        ClientResponseDto clientResponseDto = clientMapper.entityToDto(client);
        clientResponseDto.setGymName(client.getGym().getName());
        return clientResponseDto;
    }

    @Override
    public ClientResponseDto update(ClientRequestDto clientRequestDto, Long id) {

        Client existingClient = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con el ID: " + id));

        if (clientRequestDto.getName() != null) {
            existingClient.setName(clientRequestDto.getName());
        }
        if (clientRequestDto.getSurname() != null) {
            existingClient.setSurname(clientRequestDto.getSurname());
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
            existingClient.setGym(gym.orElse(null)); // Manejar caso en que el gimnasio no se encuentre
        } else {
            existingClient.setGym(null); // Establecer gimnasio en null si el nombre es null
        }


        Client updatedClient = clientRepository.save(existingClient);
        ClientResponseDto clientResponse = clientMapper.entityToDto(updatedClient);

        return clientMapper.entityToDto(updatedClient);
    }

    @Override
    public void delete(String id) {
        Client client = getClientByDniOrThrow(id);
        clientRepository.delete(client);
    }

   /* public List<RoutineResponseDto> getRoutinesByClientDni(String dni) {
        // Obtener el cliente por DNI o lanzar excepción si no existe
        Client client = getClientByDniOrThrow(dni);

        // Obtener la lista de rutinas del cliente
        List<Routine> routines = client.getRoutines();

        // Convertir las rutinas a DTO utilizando un mapper si es necesario
        return routines.stream()
                .map(routineMapper::entityToDto)
                .collect(Collectors.toList());
    }*/

    public ClientResponseDto disableClientByDni(String dni) {
        Client client = clientRepository.findByDni(dni)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con DNI: " + dni));
        client.setActive(false);
        clientRepository.save(client);

        return clientMapper.entityToDto(client);
    }

}
