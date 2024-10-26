package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.ExerciseRequestDto;
import ar.gym.gym.dto.response.ExerciseResponseDto;
import ar.gym.gym.mapper.ExerciseMapper;
import ar.gym.gym.model.Exercise;
import ar.gym.gym.model.Gym;
import ar.gym.gym.repository.ExerciseRepository;
import ar.gym.gym.service.ExerciseService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ExerciseServiceImpl implements ExerciseService {
    private ExerciseRepository exerciseRepository;
    private ExerciseMapper exerciseMapper;

    @Override
    public ExerciseResponseDto create(ExerciseRequestDto exerciseRequestDto) {
        // Verificamos si ya existe un ejercicio con el mismo nombre o id
        if (exerciseRepository.findByName(exerciseRequestDto.getName()).isPresent()) {
            throw new EntityExistsException("Ya existe un ejercicio con el nombre " + exerciseRequestDto.getName());
        }

        // Convertimos el DTO a entidad y guardamos
        Exercise exercise = exerciseMapper.dtoToEntity(exerciseRequestDto);
        exerciseRepository.save(exercise);
        // Retornamos el DTO correspondiente
        return exerciseMapper.entityToDto(exercise);
    }

    @Override
    public List<ExerciseResponseDto> findAll() {
        List<Exercise> exercises = exerciseRepository.findAll();
        return exercises.stream()
                .map(exerciseMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public Exercise getExerciseByIdOrThrow(Long id) {
        return exerciseRepository.findById(id)
                .orElseThrow(() -> new EntityExistsException("El ejercicio con ID " + id + " no existe"));
    }

    @Override
    public ExerciseResponseDto findById(Long id) {
        Exercise exercise = getExerciseByIdOrThrow(id);
        return exerciseMapper.entityToDto(exercise);
    }

    public Optional<Exercise> findByName(String name) {
        Optional<Exercise> exercise = exerciseRepository.findByName(name);
        if (exercise.isPresent()){
            return exercise;
        }
        throw new EntityExistsException("El ejercicio con el nombre " + name + " no existe");
    }



    @Override
    public ExerciseResponseDto update(ExerciseRequestDto exerciseRequestDto, Long id) {
        Exercise existingExercise = getExerciseByIdOrThrow(id);

        if (exerciseRequestDto.getName() != null && !exerciseRequestDto.getName().isEmpty()) {
            existingExercise.setName(exerciseRequestDto.getName());
        }
        if (exerciseRequestDto.getMuscleGroup() != null && !exerciseRequestDto.getMuscleGroup().isEmpty()) {
            existingExercise.setMuscleGroup(exerciseRequestDto.getMuscleGroup());
        }
        if (exerciseRequestDto.getEquipment() != null && !exerciseRequestDto.getEquipment().isEmpty()) {
            existingExercise.setEquipment(exerciseRequestDto.getEquipment());
        }
        // Guardar el ejercicio actualizado
        exerciseRepository.save(existingExercise);
        // Retornar el DTO actualizado
        return exerciseMapper.entityToDto(existingExercise);    }

    @Override
    public void delete(Long id) {
        Exercise exercise = getExerciseByIdOrThrow(id);
        exerciseRepository.delete(exercise);
    }

}
