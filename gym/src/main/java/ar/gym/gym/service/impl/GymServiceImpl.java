package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.GymRequestDto;
import ar.gym.gym.dto.response.GymResponseDto;
import ar.gym.gym.mapper.GymMapper;
import ar.gym.gym.model.Client;
import ar.gym.gym.model.Gym;
import ar.gym.gym.model.Nutritionist;
import ar.gym.gym.model.Trainer;
import ar.gym.gym.repository.ClientRepository;
import ar.gym.gym.repository.GymRepository;
import ar.gym.gym.repository.NutritionistRepository;
import ar.gym.gym.repository.TrainerRepository;
import ar.gym.gym.service.GymService;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class GymServiceImpl implements GymService {
    private GymRepository gymRepository;
    private GymMapper gymMapper;
    private ClientRepository clientRepository;
    private TrainerRepository trainerRepository;
    private NutritionistRepository nutritionistRepository;

    @Override
    @Transactional
    public GymResponseDto create(GymRequestDto gymRequestDto) {
        if (gymRepository.findByGymCode(gymRequestDto.getGymCode()).isPresent()) {
            throw new EntityExistsException("Ya existe un gimnasio con el código " + gymRequestDto.getGymCode());
        }
        Gym gym = gymMapper.dtoToEntity(gymRequestDto);
        gymRepository.save(gym);
        return gymMapper.entityToDto(gym);
    }

    @Override
    public List<GymResponseDto> findAll() {
        List<Gym> gyms = gymRepository.findAll();
        return gyms.stream()
                .map(gymMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public GymResponseDto findByName(String name) {
        // Buscar el gimnasio por nombre

        // Mapear la entidad Gym a GymResponseDto
        return gymRepository.findByName(name)
                .orElseThrow(() -> new EntityExistsException("Gym not found with name: " + name));
    }

    //Funcion para reutilizar el mensaje de clase con id inexistente
    public Gym getGymByCodeOrThrow(String gymCode) {
        return gymRepository.findByGymCode(gymCode)
                .orElseThrow(() -> new EntityExistsException("El gimnasio con el código " + gymCode + " no existe"));
    }

    @Override
    @Transactional
    public GymResponseDto update(GymRequestDto gymRequestDto) {
        // Primero obtenemos el Gym existente o lanzamos una excepción si no existe
        Gym existingGym = getGymByCodeOrThrow(gymRequestDto.getGymCode());

        // Actualizamos los campos básicos si no son nulos o vacíos
        if (gymRequestDto.getName() != null && !gymRequestDto.getName().isEmpty()) {
            existingGym.setName(gymRequestDto.getName());
        }
        if (gymRequestDto.getPhone() != null && !gymRequestDto.getPhone().isEmpty()) {
            existingGym.setPhone(gymRequestDto.getPhone());
        }
        if (gymRequestDto.getEmail() != null && !gymRequestDto.getEmail().isEmpty()) {
            existingGym.setEmail(gymRequestDto.getEmail());
        }
        if (gymRequestDto.getAddress() != null && !gymRequestDto.getAddress().isEmpty()) {
            existingGym.setAddress(gymRequestDto.getAddress());
        }

        gymRepository.save(existingGym);

        return gymMapper.entityToDto(existingGym);

    }

    @Override
    public void deleteByGymCode(String gymCode) {
        Gym gym = getGymByCodeOrThrow(gymCode);
        gymRepository.delete(gym);
    }

    @Transactional
    public GymResponseDto addClientToGym(String gymCode, String dni) {
        // Buscar el gimnasio por el código
        Gym gym = getGymByCodeOrThrow(gymCode);
        Client client = clientRepository.findByDni(dni)
                .orElseThrow(() -> new EntityExistsException("El cliente con el DNI "+ dni + "no existe") );

        // Verificar si el cliente ya está en la lista del gimnasio
        if (!gym.getClientList().contains(client)) {
            gym.getClientList().add(client); // Agregar el cliente a la lista del gimnasio
            client.setGym(gym); // Asignar el gimnasio al cliente

            // Guardar los cambios
            gymRepository.save(gym);
            clientRepository.save(client);
        } else {
            throw new EntityExistsException("El cliente ya está asignado a este gimnasio.");
        }

        return gymMapper.entityToDto(gym);
    }

    @Transactional
    public GymResponseDto addTrainerToGym(String gymCode, String dni) {
        // Buscar el gimnasio por el código
        Gym gym = getGymByCodeOrThrow(gymCode);
        Trainer trainer = trainerRepository.findByDni(dni)
                .orElseThrow(() -> new EntityExistsException("El entrenador con el DNI " + dni + " no existe"));

        // Verificar si el entrenador ya existe en la lista
        if (!gym.getTrainerList().contains(trainer)) {
            trainer.setGym(gym); // Asignar el gimnasio al entrenador
            gym.getTrainerList().add(trainer); // Añadir el entrenador a la lista de entrenadores del gimnasio

            // Guardar los cambios en la base de datos
            gymRepository.save(gym);
            trainerRepository.save(trainer);
        } else {
            throw new EntityExistsException("El entrenador ya está asignado a este gimnasio.");
        }

        // Devolver el DTO actualizado
        return gymMapper.entityToDto(gym);
    }

   @Transactional
    public GymResponseDto addNutritionistToGym(String gymCode, String dni) {
        // Buscar el gimnasio por el código
        Gym gym = getGymByCodeOrThrow(gymCode);
        Nutritionist nutritionist = nutritionistRepository.findByDni(dni)
                .orElseThrow(() -> new EntityExistsException("El entrenador con el DNI " + dni + " no existe"));

        // Verificar si el nutricionista ya existe en la lista
        if (!gym.getNutritionistList().contains(nutritionist)) {
            nutritionist.setGym(gym); // Asignar el gimnasio al nutricionista
            gym.getNutritionistList().add(nutritionist); // Añadir el nutricionista a la lista de nutricionistas del gimnasio

            // Guardar los cambios en la base de datos
            gymRepository.save(gym);
            nutritionistRepository.save(nutritionist);
        } else {
            throw new EntityExistsException("El nutricionista ya está asignado a este gimnasio.");
        }

        // Devolver el DTO actualizado
        return gymMapper.entityToDto(gym);
    }


    public void assignTrainerToClient(String dniTrainer, String dniClient){
        // Buscar entrenador y cliente por dni
        Trainer trainer = trainerRepository.findByDni(dniTrainer)
                .orElseThrow(() -> new EntityExistsException("El entrenador con el DNI " + dniTrainer + " no existe"));

        Client client = clientRepository.findByDni(dniClient)
                .orElseThrow(() -> new EntityExistsException("El cliente con el DNI " + dniClient + " no existe"));

        // Verificar que el cliente y el entrenador pertenezcan al mismo gimnasio
        if (client.getGym() == null || !client.getGym().equals(trainer.getGym())) {
            throw new IllegalArgumentException("El entrenador y el cliente deben pertenecer al mismo gimnasio.");
        }

        // Verificar si el cliente ya existe en la lista del entrenador
        if (client.getTrainer() == null || !client.getTrainer().equals(trainer)) {
            client.setTrainer(trainer); // Asignar el entrenador al cliente
            trainer.getClients().add(client); // Añadir el cliente a la lista de clientes del entrenador

            // Guardar los cambios en la base de datos
            clientRepository.save(client);
        } else {
            throw new EntityExistsException("El entrenador ya está asignado a este cliente.");
        }

    }

    public void assignNutritionistToClient(String dniNut, String dniClient){
        // Buscar entrenador y cliente por dni
        Nutritionist nutritionist = nutritionistRepository.findByDni(dniNut)
                .orElseThrow(() -> new EntityExistsException("El nutricionista con el DNI " + dniNut + " no existe"));

        Client client = clientRepository.findByDni(dniClient)
                .orElseThrow(() -> new EntityExistsException("El cliente con el DNI " + dniClient + " no existe"));

        // Verificar que el cliente y el nutricionista pertenezcan al mismo gimnasio
        if (client.getGym() == null || !client.getGym().equals(nutritionist.getGym())) {
            throw new IllegalArgumentException("El nutricionista y el cliente deben pertenecer al mismo gimnasio.");
        }

        // Verificar si el cliente ya existe en la lista del entrenador
        if (client.getNutritionist() == null || !client.getNutritionist().equals(nutritionist)) {
            client.setNutritionist(nutritionist); // Asignar el nutricionista al cliente
            nutritionist.getClients().add(client); // Añadir el cliente a la lista de clientes del nutricionista

            // Guardar los cambios en la base de datos
            clientRepository.save(client);
        } else {
            throw new EntityExistsException("El nutricionista ya está asignado a este cliente.");
        }

    }
}
