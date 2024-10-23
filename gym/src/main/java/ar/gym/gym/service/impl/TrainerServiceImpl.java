package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.RoutineRequestDto;
import ar.gym.gym.dto.request.TrainerRequestDto;
import ar.gym.gym.dto.response.ClientResponseDto;
import ar.gym.gym.dto.response.RoutineResponseDto;
import ar.gym.gym.dto.response.TrainerResponseDto;
import ar.gym.gym.mapper.ClientMapper;
import ar.gym.gym.mapper.TrainerMapper;
import ar.gym.gym.model.Trainer;
import ar.gym.gym.repository.TrainerRepository;
import ar.gym.gym.service.TrainerService;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TrainerServiceImpl implements TrainerService {
    private TrainerRepository trainerRepository;
    private TrainerMapper trainerMapper;
    private ClientMapper clientMapper;
    private RoutineServiceImpl routineServiceImpl;

    @Override
    public TrainerResponseDto create(TrainerRequestDto trainerRequestDto) {
        if(trainerRepository.findByDni(trainerRequestDto.getDni()).isPresent()){
            throw new EntityExistsException("Ya existe un entrenador con el DNI " + trainerRequestDto.getDni());
        }
        Trainer trainer = trainerMapper.dtoToEntity(trainerRequestDto);
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
    public TrainerResponseDto findById(String id) {
        Trainer trainer = getTrainerByDniOrThrow(id);
        return trainerMapper.entityToDto(trainer);
    }

    @Override
    public TrainerResponseDto update(TrainerRequestDto trainerRequestDto) {
        Trainer existingTrainer = getTrainerByDniOrThrow(trainerRequestDto.getDni());

        if (trainerRequestDto.getName() != null && !trainerRequestDto.getName().isEmpty()) {
            existingTrainer.setName(trainerRequestDto.getName());
        }
        if (trainerRequestDto.getSurname() != null && !trainerRequestDto.getSurname().isEmpty()) {
            existingTrainer.setSurname(trainerRequestDto.getSurname());
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

        existingTrainer.setActive(trainerRequestDto.isActive());
        existingTrainer.setAvailable(trainerRequestDto.isAvailable());

        // Guardamos el entrenador actualizado en la base de datos
        Trainer updatedTrainer = trainerRepository.save(existingTrainer);

        // Devolvemos el DTO actualizado usando el mapper
        return trainerMapper.entityToDto(updatedTrainer);
    }

    @Override
    public void delete(String id) {
        Trainer trainer = getTrainerByDniOrThrow(id);
        trainerRepository.delete(trainer);
    }

    public List<ClientResponseDto> getClientsAssociated(String dni){
        Trainer trainer = getTrainerByDniOrThrow(dni);
        return trainer.getClients()
                .stream()
                .map(clientMapper::entityToDto)
                .collect(Collectors.toList());
    }
    public RoutineResponseDto createRoutine(RoutineRequestDto routineRequestDto){
        return  routineServiceImpl.create(routineRequestDto);
    }
}
