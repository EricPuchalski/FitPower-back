package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.ExerciseRequestDto;
import ar.gym.gym.dto.response.ExerciseResponseDto;
import ar.gym.gym.mapper.ExerciseMapper;
import ar.gym.gym.model.Exercise;
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
        // Verificamos si ya existe un ejercicio con el mismo ID
        if (exerciseRepository.findById(exerciseRequestDto.getId()).isPresent()) {
            throw new EntityExistsException("Ya existe un ejercicio con el ID " + exerciseRequestDto.getId());
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

    public Optional<ExerciseResponseDto> findByName(String name) {
        return exerciseRepository.findByName(name)
                .or(() -> Optional.empty()); // Retorna el Optional vacío si no encuentra nada
    }



    @Override
    public ExerciseResponseDto update(ExerciseRequestDto exerciseRequestDto) {
        Exercise existingExercise = getExerciseByIdOrThrow(exerciseRequestDto.getId());
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
