package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.TrainerRequestDto;
import ar.gym.gym.dto.response.ClientResponseDto;
import ar.gym.gym.dto.response.TrainerResponseDto;
import ar.gym.gym.mapper.ClientMapper;
import ar.gym.gym.mapper.TrainerMapper;
import ar.gym.gym.model.Gym;
import ar.gym.gym.model.Trainer;
import ar.gym.gym.repository.GymRepository;
import ar.gym.gym.repository.TrainerRepository;
import ar.gym.gym.service.TrainerService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class TrainerServiceImpl implements TrainerService {
    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;
    private final ClientMapper clientMapper;

    private final GymRepository gymRepository;

    public TrainerServiceImpl(TrainerRepository trainerRepository, TrainerMapper trainerMapper, ClientMapper clientMapper, GymRepository gymRepository) {
        this.trainerRepository = trainerRepository;
        this.trainerMapper = trainerMapper;
        this.clientMapper = clientMapper;
        this.gymRepository = gymRepository;
    }

    @Override
    public TrainerResponseDto create(TrainerRequestDto trainerRequestDto) {
        if(trainerRepository.findByDni(trainerRequestDto.getDni()).isPresent()){
            throw new EntityExistsException("Ya existe un entrenador con el DNI " + trainerRequestDto.getDni());
        }

        Trainer trainer = trainerMapper.dtoToEntity(trainerRequestDto);

        if (trainerRequestDto.getGymName() != null) {
            Optional<Gym> gym = gymRepository.findByName(trainerRequestDto.getGymName());

            if (gym.isPresent()) {
                trainer.setGym(gym.get());
            } else {
                throw new EntityNotFoundException("Gimnasio no encontrado con el nombre: " + trainerRequestDto.getGymName());
            }
        }

        trainerRepository.save(trainer);
        return trainerMapper.entityToDto(trainer);
    }

    @Override
    public List<TrainerResponseDto> findAll() {
        List<Trainer> trainers = trainerRepository.findAll();
        return trainers.stream()
                .map(trainerMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public Trainer getTrainerByDniOrThrow(String dni) {
        return trainerRepository.findByDni(dni)
                .orElseThrow(() -> new EntityExistsException("El entrenador con el DNI " + dni + " no existe"));
    }

    @Override
    public TrainerResponseDto findByDni(String dni) {
        Trainer trainer = getTrainerByDniOrThrow(dni);
        return trainerMapper.entityToDto(trainer);
    }

    @Override
    public TrainerResponseDto update(TrainerRequestDto trainerRequestDto) {
        Trainer existingTrainer = getTrainerByDniOrThrow(trainerRequestDto.getDni());

        if (trainerRequestDto.getName() != null && !trainerRequestDto.getName().isEmpty()) {
            existingTrainer.setName(trainerRequestDto.getName());
        }
        if (trainerRequestDto.getLastname() != null && !trainerRequestDto.getLastname().isEmpty()) {
            existingTrainer.setLastname(trainerRequestDto.getLastname());
        }
        if (trainerRequestDto.getPhone() != null && !trainerRequestDto.getPhone().isEmpty()) {
            existingTrainer.setPhone(trainerRequestDto.getPhone());
        }
        if (trainerRequestDto.getAddress() != null && !trainerRequestDto.getAddress().isEmpty()) {
            existingTrainer.setAddress(trainerRequestDto.getAddress());
        }
        if (trainerRequestDto.getEmail() != null && !trainerRequestDto.getEmail().isEmpty()) {
            existingTrainer.setEmail(trainerRequestDto.getEmail());
        }
        if (trainerRequestDto.getProfession() != null && !trainerRequestDto.getProfession().isEmpty()) {
            existingTrainer.setProfession(trainerRequestDto.getProfession());
        }

        if (trainerRequestDto.getGymName() != null) {
            Optional<Gym> gym = gymRepository.findByName(trainerRequestDto.getGymName());

            if (gym.isPresent()) {
                existingTrainer.setGym(gym.get());
            } else {
                throw new EntityNotFoundException("Gimnasio no encontrado con el nombre: " + trainerRequestDto.getGymName());
            }
        }

        existingTrainer.setActive(trainerRequestDto.isActive());

        // Guardamos el entrenador actualizado en la base de datos
        Trainer updatedTrainer = trainerRepository.save(existingTrainer);

        // Devolvemos el DTO actualizado usando el mapper
        return trainerMapper.entityToDto(updatedTrainer);
    }

    @Override
    public void deleteByDni(String dni) {
        Trainer trainer = getTrainerByDniOrThrow(dni);
        trainerRepository.delete(trainer);
    }

    public List<ClientResponseDto> getClientsAssociated(String dni){
        Trainer trainer = getTrainerByDniOrThrow(dni);
        return trainer.getClients()
                .stream()
                .map(clientMapper::entityToDto)
                .collect(Collectors.toList());
    }
    @Override
    public List<ClientResponseDto> getClientsAssociatedEmail(String email){
        Trainer trainer = trainerRepository.findByEmail(email);
        if (trainer != null) {
            return trainer.getClients()
                    .stream()
                    .map(clientMapper::entityToDto)
                    .collect(Collectors.toList());
        }

        throw new EntityNotFoundException("Entrenador no encontrado con el email: " + email);

    }
}
