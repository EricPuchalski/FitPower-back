package ar.gym.gym.controller;

import ar.gym.gym.dto.request.ExerciseRequestDto;
import ar.gym.gym.dto.response.ExerciseResponseDto;
import ar.gym.gym.model.Exercise;
import ar.gym.gym.service.ExerciseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;
    private static final Logger logger = LoggerFactory.getLogger(ExerciseController.class);

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping
    public ResponseEntity<ExerciseResponseDto> create(@RequestBody ExerciseRequestDto exerciseRequestDto) {
        logger.info("Creating a new exercise: {}", exerciseRequestDto);
        ExerciseResponseDto createdExercise = exerciseService.create(exerciseRequestDto);
        logger.info("Exercise created successfully: {}", createdExercise);
        return new ResponseEntity<>(createdExercise, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ExerciseResponseDto>> findAll() {
        logger.info("Fetching all exercises");
        List<ExerciseResponseDto> exercises = exerciseService.findAll();
        logger.info("Retrieved {} exercises", exercises.size());
        return ResponseEntity.ok(exercises);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Exercise> findByName(@PathVariable String name) {
        logger.info("Fetching exercise with name: {}", name);
        Optional<Exercise> exercise = exerciseService.findByName(name);
        if (exercise.isPresent()) {
            logger.info("Exercise found: {}", exercise.get());
            return ResponseEntity.ok(exercise.get());
        } else {
            logger.warn("Exercise with name {} not found", name);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseResponseDto> update(@RequestBody ExerciseRequestDto exerciseRequestDto, @PathVariable Long id) {
        logger.info("Updating exercise with ID: {}", id);
        ExerciseResponseDto updatedExercise = exerciseService.update(exerciseRequestDto, id);
        logger.info("Exercise updated successfully: {}", updatedExercise);
        return ResponseEntity.ok(updatedExercise);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Deleting exercise with ID: {}", id);
        exerciseService.delete(id);
        logger.info("Exercise with ID {} has been deleted", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
