package ar.gym.gym.controller;

import ar.gym.gym.dto.response.RoutineResponseDto;
import ar.gym.gym.service.TrainingPlanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/training-plans")
public class TrainingPlanController {


    private final TrainingPlanService trainingPlanService;

    public TrainingPlanController(TrainingPlanService trainingPlanService) {
        this.trainingPlanService = trainingPlanService;
    }

    // Endpoint para completar la rutina activa y activar la siguiente
    @PostMapping("/{clientDni}/{trainingPlanId}/complete-routine")
    public ResponseEntity<String> completeCurrentRoutine(
            @PathVariable String clientDni,
            @PathVariable Long trainingPlanId) {
        // Completar la rutina activa y activar la siguiente
        trainingPlanService.completeCurrentRoutine(clientDni, trainingPlanId);
        return ResponseEntity.ok("La rutina actual ha sido completada y la siguiente rutina ha sido activada.");
    }

    // Endpoint para obtener la rutina activa actual
    @GetMapping("/{clientId}/{trainingPlanId}/active-routine")
    public ResponseEntity<RoutineResponseDto> getCurrentActiveRoutine(
            @PathVariable String clientDni,
            @PathVariable Long trainingPlanId) {
        // Obtener la rutina activa actual
        RoutineResponseDto activeRoutine = trainingPlanService.getCurrentActiveRoutine(clientDni, trainingPlanId);
        return ResponseEntity.ok(activeRoutine);
    }
}
