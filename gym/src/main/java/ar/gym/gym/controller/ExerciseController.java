package ar.gym.gym.controller;

import com.itec.FitFlowApp.dto.request.ExerciseRequestDto;
import com.itec.FitFlowApp.dto.response.ExerciseResponseDto;
import com.itec.FitFlowApp.model.service.ExerciseService;
import com.itec.FitFlowApp.util.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/fit_flow/exercises")
public class ExerciseController implements Controller<ExerciseResponseDto, ExerciseRequestDto> {
    @Autowired
    private ExerciseService exerciseService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ExerciseResponseDto> create(@RequestBody ExerciseRequestDto exerciseRequestDto) {
        ExerciseResponseDto createdExercise = exerciseService.create(exerciseRequestDto);
        return new ResponseEntity<>(createdExercise, HttpStatus.CREATED);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ExerciseResponseDto>> findAll() {
        List<ExerciseResponseDto> exercises = exerciseService.findAll();
        return ResponseEntity.ok(exercises);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ExerciseResponseDto> findById(@PathVariable String id) {
        ExerciseResponseDto exercise = exerciseService.findById(id);
        return ResponseEntity.ok(exercise);
    }

    @GetMapping("/{name}")
    public ResponseEntity<ExerciseResponseDto> findByName(@PathVariable String name) {
        ExerciseResponseDto exercise = exerciseService.findByName(name);
        return ResponseEntity.ok(exercise);
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<ExerciseResponseDto> update(@RequestBody ExerciseRequestDto exerciseRequestDto) {
        ExerciseResponseDto updatedExercise = exerciseService.update(exerciseRequestDto);
        return ResponseEntity.ok(updatedExercise);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        exerciseService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
