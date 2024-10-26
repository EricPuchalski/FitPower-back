package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.ExerciseRequestDto;
import ar.gym.gym.dto.response.ExerciseResponseDto;
import ar.gym.gym.mapper.ExerciseMapper;
import ar.gym.gym.model.Exercise;
import ar.gym.gym.repository.ExerciseRepository;
import ar.gym.gym.service.ExerciseService;
import jakarta.persistence.EntityExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExerciseServiceImpl implements ExerciseService {
    private static final Logger logger = LoggerFactory.getLogger(ExerciseServiceImpl.class);

    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;

    public ExerciseServiceImpl(ExerciseRepository exerciseRepository, ExerciseMapper exerciseMapper) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseMapper = exerciseMapper;
    }

    @Override
    public ExerciseResponseDto create(ExerciseRequestDto exerciseRequestDto) {
        logger.info("Entrando al método create con datos: {}", exerciseRequestDto);

        if (exerciseRepository.findByName(exerciseRequestDto.getName()).isPresent()) {
            logger.warn("El ejercicio con el nombre {} ya existe", exerciseRequestDto.getName());
            throw new EntityExistsException("Ya existe un ejercicio con el nombre " + exerciseRequestDto.getName());
        }

        Exercise exercise = exerciseMapper.dtoToEntity(exerciseRequestDto);
        exerciseRepository.save(exercise);

        ExerciseResponseDto response = exerciseMapper.entityToDto(exercise);
        logger.info("Saliendo del método create con respuesta: {}", response);
        return response;
    }

    @Override
    public List<ExerciseResponseDto> findAll() {
        logger.info("Entrando al método findAll");

        List<Exercise> exercises = exerciseRepository.findAll();
        List<ExerciseResponseDto> responseList = exercises.stream()
                .map(exerciseMapper::entityToDto)
                .collect(Collectors.toList());

        logger.info("Saliendo del método findAll con {} ejercicios encontrados", responseList.size());
        return responseList;
    }

    public Exercise getExerciseByIdOrThrow(Long id) {
        logger.info("Entrando al método getExerciseByIdOrThrow con ID: {}", id);

        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new EntityExistsException("El ejercicio con ID " + id + " no existe"));

        logger.info("Saliendo del método getExerciseByIdOrThrow con ejercicio: {}", exercise);
        return exercise;
    }

    @Override
    public ExerciseResponseDto findById(Long id) {
        logger.info("Entrando al método findById con ID: {}", id);

        Exercise exercise = getExerciseByIdOrThrow(id);
        ExerciseResponseDto response = exerciseMapper.entityToDto(exercise);

        logger.info("Saliendo del método findById con respuesta: {}", response);
        return response;
    }

    public Optional<Exercise> findByName(String name) {
        logger.info("Entrando al método findByName con nombre: {}", name);

        Optional<Exercise> exercise = exerciseRepository.findByName(name);

        if (exercise.isPresent()) {
            logger.info("Saliendo del método findByName con ejercicio encontrado: {}", exercise.get());
            return exercise;
        } else {
            logger.warn("El ejercicio con el nombre {} no existe", name);
            throw new EntityExistsException("El ejercicio con el nombre " + name + " no existe");
        }
    }

    @Override
    public ExerciseResponseDto update(ExerciseRequestDto exerciseRequestDto, Long id) {
        logger.info("Entrando al método update con datos: {}, ID: {}", exerciseRequestDto, id);

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

        exerciseRepository.save(existingExercise);
        ExerciseResponseDto response = exerciseMapper.entityToDto(existingExercise);

        logger.info("Saliendo del método update con respuesta: {}", response);
        return response;
    }

    @Override
    public void delete(Long id) {
        logger.info("Entrando al método delete con ID: {}", id);

        Exercise exercise = getExerciseByIdOrThrow(id);
        exerciseRepository.delete(exercise);

        logger.info("Saliendo del método delete con ejercicio eliminado: {}", id);
    }
}
