package ar.gym.gym.controller;

import ar.gym.gym.dto.request.NutritionistRequestDto;
import ar.gym.gym.dto.response.ClientResponseDto;
import ar.gym.gym.dto.response.NutritionistResponseDto;
import ar.gym.gym.service.NutritionistService;
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


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<NutritionistResponseDto> create(NutritionistRequestDto nutritionistRequestDto) {
        NutritionistResponseDto createdNutritionist = nutritionistService.create(nutritionistRequestDto);
        return new ResponseEntity<>(createdNutritionist, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<NutritionistResponseDto>> findAll() {
        List<NutritionistResponseDto> nutritionist = nutritionistService.findAll();
        return ResponseEntity.ok(nutritionist);
    }


    @GetMapping("/{dni}")
    public ResponseEntity<NutritionistResponseDto> findById(String dni) {
        NutritionistResponseDto nutritionist = nutritionistService.findByDni(dni);
        return ResponseEntity.ok(nutritionist);
    }


    @PutMapping("/{dni}")
    public ResponseEntity<NutritionistResponseDto> update(NutritionistRequestDto nutritionistRequestDto) {
        NutritionistResponseDto updatedNutritionist = nutritionistService.update(nutritionistRequestDto);
        return ResponseEntity.ok(updatedNutritionist);
    }


    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> delete(Long id) {
        nutritionistService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{dniNutritionist}")
    public ResponseEntity<List<ClientResponseDto>> getClients(@PathVariable String dniNutritionist){
        List<ClientResponseDto>nutritionistClients = nutritionistService.getClientsAssociated(dniNutritionist);
        return ResponseEntity.ok(nutritionistClients);
    }

}
