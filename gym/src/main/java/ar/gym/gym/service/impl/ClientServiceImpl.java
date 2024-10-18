package ar.gym.gym.service.impl;

import com.itec.FitFlowApp.dto.request.ClientRequestDto;
import com.itec.FitFlowApp.dto.response.ClientResponseDto;
import com.itec.FitFlowApp.exeption.EntityException;
import com.itec.FitFlowApp.mapper.ClientMapper;
import com.itec.FitFlowApp.model.entity.Client;
import com.itec.FitFlowApp.model.entity.ClientStatus;
import com.itec.FitFlowApp.model.repository.ClientRepository;
import com.itec.FitFlowApp.model.repository.ClientStatusRepository;
import com.itec.FitFlowApp.util.CRUD;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ClientServiceImpl implements CRUD<ClientResponseDto, ClientRequestDto> {
    private ClientRepository clientRepository;
    private ClientMapper clientMapper;
    private ClientStatusRepository clientStatusRepository;
    //private RoutineMapper routineMapper;

    @Override
    @Transactional
    public ClientResponseDto create(ClientRequestDto clientRequestDto) {
        if(clientRepository.findByDni(clientRequestDto.getDni()).isPresent()){
            throw new EntityException("Ya existe un cliente con el código " + clientRequestDto.getDni());
        }
        // Crear los estados inicial y actual
        ClientStatus initState = new ClientStatus();
        initState.setWeight(clientRequestDto.getInitState().getWeight());
        initState.setHeight(clientRequestDto.getInitState().getHeight());
        initState.setBodymass(clientRequestDto.getInitState().getBodymass());
        initState.setBodyfat(clientRequestDto.getInitState().getBodyfat());

        ClientStatus currentState = new ClientStatus();
        currentState.setWeight(clientRequestDto.getCurrentState().getWeight());
        currentState.setHeight(clientRequestDto.getCurrentState().getHeight());
        currentState.setBodymass(clientRequestDto.getCurrentState().getBodymass());
        currentState.setBodyfat(clientRequestDto.getCurrentState().getBodyfat());

        // Guardar los estados en la base de datos
        clientStatusRepository.save(initState);
        clientStatusRepository.save(currentState);

        Client client = clientMapper.dtoToEntity(clientRequestDto);
        client.setInitState(initState);
        client.setCurrentState(currentState);

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
                .orElseThrow(() -> new EntityException("El cliente con el dni " + dni + " no existe"));
    }
    @Override
    public ClientResponseDto findById(String id) {
        Client client = getClientByDniOrThrow(id);
        return clientMapper.entityToDto(client);
    }

    @Override
    @Transactional
    public ClientResponseDto update(ClientRequestDto clientRequestDto) {
        Client existingClient = getClientByDniOrThrow(clientRequestDto.getDni());
        if (clientRequestDto.getName() != null && !clientRequestDto.getName().isEmpty()) {
            existingClient.setName(clientRequestDto.getName());
        }
        if (clientRequestDto.getSurname() != null && !clientRequestDto.getSurname().isEmpty()) {
            existingClient.setSurname(clientRequestDto.getSurname());
        }
        if (clientRequestDto.getPhone() != null && !clientRequestDto.getPhone().isEmpty()) {
            existingClient.setPhone(clientRequestDto.getPhone());
        }
        if (clientRequestDto.getAddress() != null && !clientRequestDto.getAddress().isEmpty()) {
            existingClient.setAddress(clientRequestDto.getAddress());
        }
        if (clientRequestDto.getEmail() != null && !clientRequestDto.getEmail().isEmpty()) {
            existingClient.setEmail(clientRequestDto.getEmail());
        }
        if (clientRequestDto.getGoal() != null && !clientRequestDto.getGoal().isEmpty()) {
            existingClient.setGoal(clientRequestDto.getGoal());
        }
        if (clientRequestDto.getInitState() != null) {
            existingClient.setInitState(clientRequestDto.getInitState());
        }
        if (clientRequestDto.getCurrentState() != null) {
            existingClient.setCurrentState(clientRequestDto.getCurrentState());
        }

        // Actualizar el estado de actividad del cliente
        existingClient.setActive(clientRequestDto.isActive());

        // Guardamos el cliente actualizado en la base de datos
        Client updatedClient = clientRepository.save(existingClient);

        // Devolvemos el DTO actualizado usando el mapper
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



}
