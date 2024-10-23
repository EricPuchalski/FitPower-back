package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.ClientRequestDto;
import ar.gym.gym.dto.response.ClientResponseDto;
import ar.gym.gym.mapper.ClientMapper;
import ar.gym.gym.model.Client;
import ar.gym.gym.model.ClientStatus;
import ar.gym.gym.repository.ClientRepository;
import ar.gym.gym.repository.ClientStatusRepository;
import ar.gym.gym.service.ClientService;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {
    private ClientRepository clientRepository;
    private ClientMapper clientMapper;
    private ClientStatusRepository clientStatusRepository;
    //private RoutineMapper routineMapper;

    @Override
    public ClientResponseDto create(ClientRequestDto clientRequestDto) {
        if(clientRepository.findByDni(clientRequestDto.getDni()).isPresent()){
            throw new EntityExistsException("Ya existe un cliente con el código " + clientRequestDto.getDni());
        }
        // Crear los estados inicial y actual
//        ClientStatus initState = new ClientStatus();
//        initState.setWeight(clientRequestDto.getInitState().getWeight());
//        initState.setHeight(clientRequestDto.getInitState().getHeight());
//        initState.setBodymass(clientRequestDto.getInitState().getBodymass());
//        initState.setBodyfat(clientRequestDto.getInitState().getBodyfat());
//
        ClientStatus currentState = new ClientStatus();
//        currentState.setWeight(clientRequestDto.getCurrentState().getWeight());
//        currentState.setHeight(clientRequestDto.getCurrentState().getHeight());
//        currentState.setBodymass(clientRequestDto.getCurrentState().getBodymass());
//        currentState.setBodyfat(clientRequestDto.getCurrentState().getBodyfat());

        // Guardar los estados en la base de datos
//        clientStatusRepository.save(initState);
//        clientStatusRepository.save(currentState);

        Client client = clientMapper.dtoToEntity(clientRequestDto);
//        client.setInitState(initState);
//        client.setCurrentState(currentState);

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
    public ClientResponseDto findById(String id) {
        Client client = getClientByDniOrThrow(id);
        return clientMapper.entityToDto(client);
    }

    @Override
    @Transactional
    public ClientResponseDto update(ClientRequestDto clientRequestDto) {
        Client existingClient = clientMapper.dtoToEntity(clientRequestDto);
        clientRepository.save(existingClient);
        return clientMapper.entityToDto(existingClient);
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



}
