package ar.gym.gym.controller;

import ar.gym.gym.dto.request.TrainingPlanRequestDto;
import ar.gym.gym.dto.request.TrainingPlanUpdateRequestDto;
import ar.gym.gym.dto.response.RoutineResponseDto;
import ar.gym.gym.dto.response.TrainingPlanResponseDto;
import ar.gym.gym.service.TrainingPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training-plans")
public class TrainingPlanController {

    private final Logger logger = LoggerFactory.getLogger(TrainingPlanController.class);
    private final TrainingPlanService trainingPlanService;

    public TrainingPlanController(TrainingPlanService trainingPlanService) {
        this.trainingPlanService = trainingPlanService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<TrainingPlanResponseDto> createTrainingPlan(@Validated @RequestBody TrainingPlanRequestDto trainingPlanRequestDto) {
        logger.info("Recibiendo solicitud para crear un plan de entrenamiento");
        TrainingPlanResponseDto response = trainingPlanService.create(trainingPlanRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<TrainingPlanResponseDto>> getAllTrainingPlans() {
        logger.info("Recibiendo solicitud para obtener todos los planes de entrenamiento");
        List<TrainingPlanResponseDto> response = trainingPlanService.findAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<TrainingPlanResponseDto> getTrainingPlanById(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para obtener el plan de entrenamiento con ID: {}", id);
        TrainingPlanResponseDto response = trainingPlanService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<TrainingPlanResponseDto> updateTrainingPlan(
            @PathVariable Long id,
            @Validated @RequestBody TrainingPlanUpdateRequestDto trainingPlanRequestDto) {
        logger.info("Recibiendo solicitud para actualizar el plan de entrenamiento con ID: {}", id);
        TrainingPlanResponseDto response = trainingPlanService.update(id, trainingPlanRequestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<Void> deleteTrainingPlan(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para eliminar el plan de entrenamiento con ID: {}", id);
        trainingPlanService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{trainingPlanId}/routines/{routineId}")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<TrainingPlanResponseDto> addRoutineToPlan(
            @PathVariable Long trainingPlanId,
            @PathVariable Long routineId) {
        logger.info("Recibiendo solicitud para agregar una rutina al plan de entrenamiento con ID del plan: {} y ID de la rutina: {}", trainingPlanId, routineId);
        TrainingPlanResponseDto response = trainingPlanService.addRoutineToPlan(trainingPlanId, routineId);
        return ResponseEntity.ok(response);
    }



    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<TrainingPlanResponseDto> deactivateTrainingPlan(@PathVariable Long id) {
        logger.info("Recibiendo solicitud para desactivar el plan de entrenamiento con ID: {}", id);
        TrainingPlanResponseDto response = trainingPlanService.deactivateTrainingPlan(id);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{trainingPlanId}/active-routines")
    public List<RoutineResponseDto> getActiveRoutinesByTrainingPlan(@PathVariable Long trainingPlanId) {
        logger.info("Request received for active routines of training plan ID: {}", trainingPlanId);
        return trainingPlanService.findActiveRoutinesByTrainingPlan(trainingPlanId);
    }

    @GetMapping("/active/{dni}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_TRAINER')")
    public ResponseEntity<TrainingPlanResponseDto> getActiveTrainingPlanByDni(@PathVariable String dni) {
        logger.info("Solicitud para obtener el plan de entrenamiento activo para el cliente con DNI: {}", dni);

        TrainingPlanResponseDto activeTrainingPlan = trainingPlanService.findActiveTrainingPlanByClientDni(dni);

        return ResponseEntity.ok(activeTrainingPlan);
    }

    @GetMapping("/client/{dni}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_TRAINER')")
    public ResponseEntity<List<TrainingPlanResponseDto>> getTrainingPlansByClientDni(@PathVariable String dni) {
        List<TrainingPlanResponseDto> trainingPlans = trainingPlanService.findAllTrainingPlansByClientDni(dni);
        return ResponseEntity.ok(trainingPlans);
    }


}