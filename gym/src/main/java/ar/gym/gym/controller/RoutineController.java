package ar.gym.gym.controller;

import ar.gym.gym.dto.request.RoutineRequestDto;
import ar.gym.gym.dto.request.SessionRequestDto;
import ar.gym.gym.dto.response.RoutineResponseDto;
import ar.gym.gym.service.RoutineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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


    // Endpoint to create a new routine
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<RoutineResponseDto> createRoutine(@RequestBody RoutineRequestDto routineRequestDto) {
        RoutineResponseDto createdRoutine = routineService.create(routineRequestDto);
        return new ResponseEntity<>(createdRoutine, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<RoutineResponseDto> completeRoutine(@PathVariable Long id) {
        RoutineResponseDto responseDto = routineService.completeRoutine(id);
        return ResponseEntity.ok(responseDto);
    }

    // Endpoint to activate a routine and set all others to inactive
    @PostMapping("/activate/{clientDni}/{routineId}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<Void> activateRoutine(@PathVariable String clientDni, @PathVariable Long routineId) {
        routineService.activateRoutine(clientDni, routineId);
        return ResponseEntity.ok().build();
    }

    // Endpoint to get the active routine of a client
    @GetMapping("/active/{clientDni}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<RoutineResponseDto> getActiveRoutine(@PathVariable String clientDni) {
        RoutineResponseDto activeRoutine = routineService.getActiveRoutine(clientDni);
        return ResponseEntity.ok(activeRoutine);
    }
    @GetMapping("/active/email/{email}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<RoutineResponseDto> getActiveRoutineByEmail(@PathVariable String email) {
        RoutineResponseDto activeRoutine = routineService.getActiveRoutineByEmail(email);
        return ResponseEntity.ok(activeRoutine);
    }

    // Endpoint to get all routines of a client by their DNI
    @GetMapping("/client/{clientDni}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_TRAINER')")
    public ResponseEntity<List<RoutineResponseDto>> getRoutinesByClientDni(@PathVariable String clientDni) {
        List<RoutineResponseDto> routines = routineService.getRoutinesByClientDni(clientDni);
        return ResponseEntity.ok(routines);
    }

    @GetMapping("/client/email/{clientEmail}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<RoutineResponseDto>> getRoutinesByClientEmail(@PathVariable String clientEmail) {
        List<RoutineResponseDto> routines = routineService.getRoutinesByClientEmail(clientEmail);
        return ResponseEntity.ok(routines);
    }


    @GetMapping
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<List<RoutineResponseDto>> getAllRoutines() {
        List<RoutineResponseDto> routines = routineService.findAll();
        return new ResponseEntity<>(routines, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_TRAINER')")
    public ResponseEntity<RoutineResponseDto> getRoutineById(@PathVariable Long id) {
        RoutineResponseDto routine = routineService.findById(id);
        return new ResponseEntity<>(routine, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<RoutineResponseDto> updateRoutine(@PathVariable Long id,
                                                            @RequestBody RoutineRequestDto routineRequestDto) {
        RoutineResponseDto updatedRoutine = routineService.update(routineRequestDto, id);
        return new ResponseEntity<>(updatedRoutine, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<Void> deleteRoutine(@PathVariable Long id) {
        routineService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{routineId}/sessions")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<RoutineResponseDto> addSessionToRoutine(@PathVariable Long routineId, @RequestBody SessionRequestDto sessionRequestDto) {
        RoutineResponseDto updatedRoutine = routineService.addSessionToRoutine(routineId, sessionRequestDto);
        return new ResponseEntity<>(updatedRoutine, HttpStatus.OK);
    }

    @DeleteMapping("/{routineId}/sessions/{sessionId}")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<RoutineResponseDto> removeSessionFromRoutine(@PathVariable Long routineId,
                                                                       @PathVariable Long sessionId) {
        RoutineResponseDto updatedRoutine = routineService.removeSessionFromRoutine(routineId, sessionId);
        return new ResponseEntity<>(updatedRoutine, HttpStatus.OK);
    }

    @PutMapping("/{routineId}/sessions/{sessionId}")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<RoutineResponseDto> editSessionInRoutine(
            @PathVariable Long routineId,
            @PathVariable Long sessionId,
            @RequestBody SessionRequestDto sessionRequestDto) {

        // Llamamos al servicio para editar la sesión en la rutina
        RoutineResponseDto updatedRoutine = routineService.editSessionInRoutine(routineId, sessionId, sessionRequestDto);

        // Devolvemos la respuesta con el DTO de la rutina actualizada
        return ResponseEntity.ok(updatedRoutine);
    }

    @PostMapping("/deactivate/{routineId}")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<Void> deactivateRoutine(@PathVariable Long routineId) {
        routineService.deactivateRoutine(routineId); // Llamar al servicio para desactivar la rutina
        return ResponseEntity.ok().build(); // Retorna una respuesta exitosa
    }

}

