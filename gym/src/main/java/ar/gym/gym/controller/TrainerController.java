package ar.gym.gym.controller;

import ar.gym.gym.dto.request.TrainerRequestDto;
import ar.gym.gym.dto.response.ClientResponseDto;
import ar.gym.gym.dto.response.TrainerResponseDto;
import ar.gym.gym.service.TrainerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {

    private static final Logger logger = LoggerFactory.getLogger(TrainerController.class);
    private final TrainerService trainerService;

    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TrainerResponseDto> create(@Valid @RequestBody TrainerRequestDto trainerRequestDto) {
        logger.info("Creating trainer with request: {}", trainerRequestDto);
        TrainerResponseDto createdTrainer = trainerService.create(trainerRequestDto);
        logger.info("Created trainer: {}", createdTrainer);
        return new ResponseEntity<>(createdTrainer, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<TrainerResponseDto>> findAll() {
        logger.info("Retrieving all trainers");
        List<TrainerResponseDto> trainers = trainerService.findAll();
        logger.info("Retrieved trainers: {}", trainers);
        return ResponseEntity.ok(trainers);
    }

    @GetMapping("/{dni}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TrainerResponseDto> findByzDni(@PathVariable String dni) {
        logger.info("Finding trainer by DNI: {}", dni);
        TrainerResponseDto trainer = trainerService.findByDni(dni);
        logger.info("Found trainer: {}", trainer);
        return ResponseEntity.ok(trainer);
    }

    @PutMapping("/{dni}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TrainerResponseDto> update(@Valid @RequestBody TrainerRequestDto trainerRequestDto, @PathVariable String dni) {
        logger.info("Updating trainer with DNI: {} and request: {}", dni, trainerRequestDto);
        TrainerResponseDto updatedTrainer = trainerService.update(trainerRequestDto);
        logger.info("Updated trainer: {}", updatedTrainer);
        return ResponseEntity.ok(updatedTrainer);
    }

    @DeleteMapping("/{dni}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteByDni(@PathVariable String dni) {
        logger.info("Deleting trainer with DNI: {}", dni);
        trainerService.deleteByDni(dni);
        logger.info("Deleted trainer with DNI: {}", dni);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/clients-by-dni/{dni}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TRAINER')")
    public ResponseEntity<List<ClientResponseDto>> getClients(@PathVariable String dni) {
        logger.info("Retrieving clients associated with trainer DNI: {}", dni);
        List<ClientResponseDto> trainerClients = trainerService.getClientsAssociated(dni);
        logger.info("Retrieved clients: {}", trainerClients);
        return ResponseEntity.ok(trainerClients);
    }

    @GetMapping("/clients-by-email/{trainerEmail}")
    @PreAuthorize("hasRole('ROLE_TRAINER')")
    public ResponseEntity<List<ClientResponseDto>> getClientsByEmail(@PathVariable String trainerEmail) {
        logger.info("Retrieving clients associated with trainer email: {}", trainerEmail);
        List<ClientResponseDto> trainerClients = trainerService.getClientsAssociatedEmail(trainerEmail);
        logger.info("Retrieved clients: {}", trainerClients);
        return ResponseEntity.ok(trainerClients);
    }




    //falta crear la funcion de plan nut
    //creamos rutina y agregamos a trainer
}
