package ar.gym.gym.controller;

import ar.gym.gym.dto.request.GymRequestDto;
import ar.gym.gym.dto.response.AddClientToNutritionistResponseDto;
import ar.gym.gym.dto.response.AddClientToTrainerResponseDto;
import ar.gym.gym.dto.response.GymResponseDto;
import ar.gym.gym.model.Gym;
import ar.gym.gym.service.GymService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/gyms")
public class GymController {

    private final GymService gymService;
    private static final Logger logger = LoggerFactory.getLogger(GymController.class);

    public GymController(GymService gymService) {
        this.gymService = gymService;
    }

    // Endpoint para crear un nuevo gimnasio
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GymResponseDto> create(@RequestBody GymRequestDto gymRequestDto) {
        logger.info("Creating a new gym: {}", gymRequestDto);
        GymResponseDto createdGym = gymService.create(gymRequestDto);
        logger.info("Gym created successfully: {}", createdGym);
        return new ResponseEntity<>(createdGym, HttpStatus.CREATED);
    }

    // Endpoint para obtener todos los gimnasios
    @GetMapping
    public ResponseEntity<List<GymResponseDto>> findAll() {
        logger.info("Fetching all gyms");
        List<GymResponseDto> gyms = gymService.findAll();
        logger.info("Retrieved {} gyms", gyms.size());
        return ResponseEntity.ok(gyms);
    }

    // Endpoint para obtener un gimnasio por código
    @GetMapping("/{name}")
    public ResponseEntity<Gym> findByName(@PathVariable String name) {
        logger.info("Fetching gym with name: {}", name);
        Optional<Gym> gym = gymService.findByName(name);
        if (gym.isPresent()) {
            logger.info("Gym found: {}", gym.get());
            return ResponseEntity.ok(gym.get());
        } else {
            logger.warn("Gym with name {} not found", name);
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoint para actualizar un gimnasio existente
    @PutMapping("/{id}")
    public ResponseEntity<GymResponseDto> update(@RequestBody GymRequestDto gymRequestDto, @PathVariable Long id) {
        logger.info("Updating gym with ID: {}", id);
        GymResponseDto updatedGym = gymService.update(gymRequestDto, id);
        logger.info("Gym updated successfully: {}", updatedGym);
        return ResponseEntity.ok(updatedGym);
    }

    // Endpoint para eliminar un gimnasio
    @DeleteMapping("/{gymCode}")
    public ResponseEntity<Void> delete(@PathVariable String gymCode) {
        logger.info("Deleting gym with code: {}", gymCode);
        gymService.deleteByGymCode(gymCode);
        logger.info("Gym with code {} has been deleted", gymCode);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Endpoint para agregar un cliente a un gimnasio
    @PutMapping("/add/{gymCode}/clients/{dni}")
    public ResponseEntity<GymResponseDto> addClientToGym(@PathVariable String gymCode, @PathVariable String dni) {
        logger.info("Adding client with DNI {} to gym with code {}", dni, gymCode);
        GymResponseDto updatedGym = gymService.addClientToGym(gymCode, dni);
        logger.info("Client added to gym successfully: {}", updatedGym);
        return ResponseEntity.ok(updatedGym);
    }

    // Endpoint para agregar un entrenador a un gimnasio
    @PostMapping("/add/{gymCode}/trainers/{dni}")
    public ResponseEntity<GymResponseDto> addTrainerToGym(@PathVariable String gymCode, @PathVariable String dni) {
        logger.info("Adding trainer with DNI {} to gym with code {}", dni, gymCode);
        GymResponseDto updatedGym = gymService.addTrainerToGym(gymCode, dni);
        logger.info("Trainer added to gym successfully: {}", updatedGym);
        return ResponseEntity.ok(updatedGym);
    }

    // Endpoint para agregar un nutricionista a un gimnasio
    @PostMapping("/add/{gymCode}/nutritionist/{dni}")
    public ResponseEntity<GymResponseDto> addNutritionistToGym(@PathVariable String gymCode, @PathVariable String dni) {
        logger.info("Adding nutritionist with DNI {} to gym with code {}", dni, gymCode);
        GymResponseDto updatedGym = gymService.addNutritionistToGym(gymCode, dni);
        logger.info("Nutritionist added to gym successfully: {}", updatedGym);
        return ResponseEntity.ok(updatedGym);
    }

    // Endpoint para asignar un entrenador a un cliente
    @PostMapping("/assign/{dniTrainer}/trainer-to-client/{dniClient}")
    public ResponseEntity<AddClientToTrainerResponseDto> assignTrainerToClient(@PathVariable String dniTrainer, @PathVariable String dniClient) {
        logger.info("Assigning trainer with DNI {} to client with DNI {}", dniTrainer, dniClient);
        AddClientToTrainerResponseDto addClientToTrainerResponseDto = gymService.assignTrainerToClient(dniTrainer, dniClient);
        logger.info("Trainer assigned to client successfully: {}", addClientToTrainerResponseDto);
        return ResponseEntity.ok(addClientToTrainerResponseDto);
    }

    // Endpoint para asignar un nutricionista a un cliente
    @PutMapping("/assign/{dniNut}/nutritionist-to-client/{dniClient}")
    public ResponseEntity<AddClientToNutritionistResponseDto> assignNutritionistToClient(@PathVariable String dniNut, @PathVariable String dniClient) {
        logger.info("Assigning nutritionist with DNI {} to client with DNI {}", dniNut, dniClient);
        AddClientToNutritionistResponseDto addClientToNutritionistResponseDto = gymService.assignNutritionistToClient(dniNut, dniClient);
        logger.info("Nutritionist assigned to client successfully: {}", addClientToNutritionistResponseDto);
        return ResponseEntity.ok(addClientToNutritionistResponseDto);
    }
}
