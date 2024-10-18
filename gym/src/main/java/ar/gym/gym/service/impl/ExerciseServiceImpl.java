package ar.gym.gym.service.impl;

import com.itec.FitFlowApp.dto.request.ExerciseRequestDto;
import com.itec.FitFlowApp.dto.response.ExerciseResponseDto;
import com.itec.FitFlowApp.exeption.EntityException;
import com.itec.FitFlowApp.mapper.ExerciseMapper;
import com.itec.FitFlowApp.model.entity.Exercise;
import com.itec.FitFlowApp.model.repository.ExerciseRepository;
import com.itec.FitFlowApp.util.CRUD;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ExerciseServiceImpl implements CRUD<ExerciseResponseDto, ExerciseRequestDto> {
    private ExerciseRepository exerciseRepository;
    private ExerciseMapper exerciseMapper;

    @Override
    @Transactional
    public ExerciseResponseDto create(ExerciseRequestDto exerciseRequestDto) {
        // Verificamos si ya existe un ejercicio con el mismo nombre o id
        if (exerciseRepository.findByName(exerciseRequestDto.getName()).isPresent()) {
            throw new EntityException("Ya existe un ejercicio con el nombre " + exerciseRequestDto.getName());
        }
        // Verificamos si ya existe un ejercicio con el mismo ID
        if (exerciseRepository.findById(exerciseRequestDto.getId()).isPresent()) {
            throw new EntityException("Ya existe un ejercicio con el ID " + exerciseRequestDto.getId());
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
                .orElseThrow(() -> new EntityException("El ejercicio con ID " + id + " no existe"));
    }
    public Exercise getExerciseByNameOrThrow(String name) {
        return exerciseRepository.findByName(name)
                .orElseThrow(() -> new EntityException("El ejercicio con el nombre " + name + " no existe"));
    }

    @Override
    public ExerciseResponseDto findById(String id) {
        Exercise exercise = getExerciseByIdOrThrow(Long.parseLong(id));
        return exerciseMapper.entityToDto(exercise);
    }

    public ExerciseResponseDto findByName(String name){
        Exercise exercise = getExerciseByNameOrThrow(name);
        return exerciseMapper.entityToDto(exercise);
    }

    @Override
    @Transactional
    public ExerciseResponseDto update(ExerciseRequestDto exerciseRequestDto) {
        Exercise existingExercise = getExerciseByIdOrThrow(exerciseRequestDto.getId());
        // Actualizar los atributos si no son nulos o vacíos
        if (exerciseRequestDto.getName() != null && !exerciseRequestDto.getName().isEmpty()) {
            existingExercise.setName(exerciseRequestDto.getName());
        }

        if (exerciseRequestDto.getEquipment() != null && !exerciseRequestDto.getEquipment().isEmpty()) {
            existingExercise.setEquipment(exerciseRequestDto.getEquipment());
        }

        if (exerciseRequestDto.getWeight() != 0) {
            existingExercise.setWeight(exerciseRequestDto.getWeight());
        }

        if (exerciseRequestDto.getRestTime() != null) {
            existingExercise.setRestTime(exerciseRequestDto.getRestTime());
        }

        if (exerciseRequestDto.getDuration() != null) {
            existingExercise.setDuration(exerciseRequestDto.getDuration());
        }

        if (exerciseRequestDto.getSession() != null) {
            existingExercise.setSession(exerciseRequestDto.getSession());
        }

        // Guardar el ejercicio actualizado
        exerciseRepository.save(existingExercise);

        // Retornar el DTO actualizado
        return exerciseMapper.entityToDto(existingExercise);    }

    @Override
    public void delete(String id) {
        Exercise exercise = getExerciseByIdOrThrow(Long.parseLong(id));
        exerciseRepository.delete(exercise);
    }
}
