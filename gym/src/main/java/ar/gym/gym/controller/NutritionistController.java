package ar.gym.gym.controller;

import ar.gym.gym.dto.request.NutritionistRequestDto;
import ar.gym.gym.dto.response.ClientResponseDto;
import ar.gym.gym.dto.response.NutritionistResponseDto;
import ar.gym.gym.service.NutritionistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nutritionists")
public class NutritionistController {

    @Autowired
    private NutritionistService nutritionistService;
    private static final Logger logger = LoggerFactory.getLogger(NutritionistController.class);

    @PostMapping
    public ResponseEntity<NutritionistResponseDto> create(@RequestBody NutritionistRequestDto nutritionistRequestDto) {
        logger.info("Creating nutritionist with request: {}", nutritionistRequestDto);
        NutritionistResponseDto createdNutritionist = nutritionistService.create(nutritionistRequestDto);
        logger.info("Created nutritionist: {}", createdNutritionist);
        return new ResponseEntity<>(createdNutritionist, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<NutritionistResponseDto>> findAll() {
        logger.info("Retrieving all nutritionists");
        List<NutritionistResponseDto> nutritionists = nutritionistService.findAll();
        logger.info("Retrieved nutritionists: {}", nutritionists);
        return ResponseEntity.ok(nutritionists);
    }

    @GetMapping("/{dni}")
    public ResponseEntity<NutritionistResponseDto> findById(@PathVariable String dni) {
        logger.info("Finding nutritionist by DNI: {}", dni);
        NutritionistResponseDto nutritionist = nutritionistService.findByDni(dni);
        logger.info("Found nutritionist: {}", nutritionist);
        return ResponseEntity.ok(nutritionist);
    }

    @PutMapping("/{dni}")
    public ResponseEntity<NutritionistResponseDto> update(@RequestBody NutritionistRequestDto nutritionistRequestDto, @PathVariable String dni) {
        logger.info("Updating nutritionist with DNI: {} and request: {}", dni, nutritionistRequestDto);
        NutritionistResponseDto updatedNutritionist = nutritionistService.update(nutritionistRequestDto);
        logger.info("Updated nutritionist: {}", updatedNutritionist);
        return ResponseEntity.ok(updatedNutritionist);
    }

    @PutMapping("/disable/{dni}")
    public ResponseEntity<NutritionistResponseDto> disableByDni(@PathVariable String dni) {
        logger.info("Disabling nutritionist with DNI: {}", dni);
        NutritionistResponseDto nutritionistRequestDto = nutritionistService.disableNutritionistByDni(dni);
        logger.info("Disabled nutritionist: {}", nutritionistRequestDto);
        return ResponseEntity.ok(nutritionistRequestDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        logger.info("Deleting nutritionist with ID: {}", id);
        nutritionistService.delete(id);
        logger.info("Deleted nutritionist with ID: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/clients/{dni}")
    public ResponseEntity<List<ClientResponseDto>> getClientsAssociated(@PathVariable String dni) {
        logger.info("Retrieving clients associated with nutritionist DNI: {}", dni);
        List<ClientResponseDto> nutritionistClients = nutritionistService.getClientsAssociated(dni);
        logger.info("Retrieved clients: {}", nutritionistClients);
        return ResponseEntity.ok(nutritionistClients);
    }

}
