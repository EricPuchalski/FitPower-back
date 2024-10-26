package ar.gym.gym.controller;

import ar.gym.gym.dto.request.ExerciseRequestDto;
import ar.gym.gym.dto.response.ExerciseResponseDto;
import ar.gym.gym.model.Exercise;
import ar.gym.gym.service.ExerciseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private final ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping
    public ResponseEntity<ExerciseResponseDto> create(@RequestBody ExerciseRequestDto exerciseRequestDto) {
        ExerciseResponseDto createdExercise = exerciseService.create(exerciseRequestDto);
        return new ResponseEntity<>(createdExercise, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<ExerciseResponseDto>> findAll() {
        List<ExerciseResponseDto> exercises = exerciseService.findAll();
        return ResponseEntity.ok(exercises);
    }



    @GetMapping("/{name}")
    public ResponseEntity<Exercise> findByName(@PathVariable String name) {
        Optional<Exercise> exercise = exerciseService.findByName(name);
        return ResponseEntity.ok(exercise.get());
    }



    @PutMapping("/{id}")    public ResponseEntity<ExerciseResponseDto> update(@RequestBody ExerciseRequestDto exerciseRequestDto, @PathVariable Long id) {
        ExerciseResponseDto updatedExercise = exerciseService.update(exerciseRequestDto, id);
        return ResponseEntity.ok(updatedExercise);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        exerciseService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
