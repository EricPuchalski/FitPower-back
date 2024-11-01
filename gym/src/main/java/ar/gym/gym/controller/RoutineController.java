package ar.gym.gym.controller;

import ar.gym.gym.dto.request.RoutineRequestDto;
import ar.gym.gym.dto.request.SessionRequestDto;
import ar.gym.gym.dto.response.RoutineResponseDto;
import ar.gym.gym.service.RoutineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routines")
public class RoutineController {

    private final RoutineService routineService;

    @Autowired
    public RoutineController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @PostMapping
    public ResponseEntity<RoutineResponseDto> createRoutine(@RequestBody RoutineRequestDto routineRequestDto) {
        RoutineResponseDto createdRoutine = routineService.create(routineRequestDto);
        return new ResponseEntity<>(createdRoutine, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoutineResponseDto>> getAllRoutines() {
        List<RoutineResponseDto> routines = routineService.findAll();
        return new ResponseEntity<>(routines, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoutineResponseDto> getRoutineById(@PathVariable Long id) {
        RoutineResponseDto routine = routineService.findById(id);
        return new ResponseEntity<>(routine, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoutineResponseDto> updateRoutine(@PathVariable Long id,
                                                            @RequestBody RoutineRequestDto routineRequestDto) {
        RoutineResponseDto updatedRoutine = routineService.update(routineRequestDto, id);
        return new ResponseEntity<>(updatedRoutine, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable Long id) {
        routineService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{routineId}/sessions")
    public ResponseEntity<RoutineResponseDto> addSessionToRoutine(@PathVariable Long routineId, @RequestBody SessionRequestDto sessionRequestDto, @RequestParam String exerciseName) {
        RoutineResponseDto updatedRoutine = routineService.addSessionToRoutine(routineId, sessionRequestDto, exerciseName);
        return new ResponseEntity<>(updatedRoutine, HttpStatus.OK);
    }

    @DeleteMapping("/{routineId}/sessions/{sessionId}")
    public ResponseEntity<RoutineResponseDto> removeSessionFromRoutine(@PathVariable Long routineId,
                                                                       @PathVariable Long sessionId) {
        RoutineResponseDto updatedRoutine = routineService.removeSessionFromRoutine(routineId, sessionId);
        return new ResponseEntity<>(updatedRoutine, HttpStatus.OK);
    }

    @PutMapping("/{routineId}/sessions")
    public ResponseEntity<RoutineResponseDto> editSessionInRoutine(@PathVariable Long routineId,
                                                                   @RequestBody SessionRequestDto sessionRequestDto) {
        RoutineResponseDto updatedRoutine = routineService.editSessionInRoutine(routineId, sessionRequestDto);
        return new ResponseEntity<>(updatedRoutine, HttpStatus.OK);
    }
}

