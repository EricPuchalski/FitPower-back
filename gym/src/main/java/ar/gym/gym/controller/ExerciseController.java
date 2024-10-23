package ar.gym.gym.controller;

import ar.gym.gym.dto.request.ExerciseRequestDto;
import ar.gym.gym.dto.response.ExerciseResponseDto;
import ar.gym.gym.service.ExerciseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {

    private ExerciseService exerciseService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ExerciseResponseDto> create(@RequestBody ExerciseRequestDto exerciseRequestDto) {
        ExerciseResponseDto createdExercise = exerciseService.create(exerciseRequestDto);
        return new ResponseEntity<>(createdExercise, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<ExerciseResponseDto>> findAll() {
        List<ExerciseResponseDto> exercises = exerciseService.findAll();
        return ResponseEntity.ok(exercises);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponseDto> findById(@PathVariable Long id) {
        ExerciseResponseDto exercise = exerciseService.findById(id);
        return ResponseEntity.ok(exercise);
    }

    @GetMapping("/{name}")
    public ResponseEntity<ExerciseResponseDto> findByName(@PathVariable String name) {
        Optional<ExerciseResponseDto> exercise = exerciseService.findByName(name);
        return exercise.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    @PutMapping("/{id}")
    public ResponseEntity<ExerciseResponseDto> update(@RequestBody ExerciseRequestDto exerciseRequestDto) {
        ExerciseResponseDto updatedExercise = exerciseService.update(exerciseRequestDto);
        return ResponseEntity.ok(updatedExercise);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        exerciseService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
