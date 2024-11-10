package ar.gym.gym.controller;

import ar.gym.gym.dto.request.NutritionPlanRequestDto;
import ar.gym.gym.dto.response.NutritionPlanResponseDto;
import ar.gym.gym.service.NutritionPlanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/nutrition-plans")
public class NutritionPlanController {

    private final NutritionPlanService nutritionPlanService;

    @Autowired
    public NutritionPlanController(NutritionPlanService nutritionPlanService) {
        this.nutritionPlanService = nutritionPlanService;
    }

    @PostMapping
    public ResponseEntity<NutritionPlanResponseDto> createNutritionPlan(@Valid @RequestBody NutritionPlanRequestDto nutritionPlanRequestDto) {
        NutritionPlanResponseDto createdNutritionPlan = nutritionPlanService.createNutritionPlan(nutritionPlanRequestDto);
        return new ResponseEntity<>(createdNutritionPlan, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<NutritionPlanResponseDto>> findAllNutritionPlans() {
        List<NutritionPlanResponseDto> nutritionPlans = nutritionPlanService.findAllNutritionPlans();
        return new ResponseEntity<>(nutritionPlans, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NutritionPlanResponseDto> findNutritionPlanById(@PathVariable Long id) {
        Optional<NutritionPlanResponseDto> nutritionPlan = nutritionPlanService.findNutritionPlanById(id);
        return nutritionPlan.map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NutritionPlanResponseDto> updateNutritionPlan(@PathVariable Long id, @Valid @RequestBody NutritionPlanRequestDto nutritionPlanRequestDto) {
        NutritionPlanResponseDto updatedNutritionPlan = nutritionPlanService.updateNutritionPlan(id, nutritionPlanRequestDto);
        return new ResponseEntity<>(updatedNutritionPlan, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNutritionPlan(@PathVariable Long id) {
        nutritionPlanService.deleteNutritionPlan(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<NutritionPlanResponseDto>> findCompletedNutritionPlans() {
        List<NutritionPlanResponseDto> nutritionPlans = nutritionPlanService.findCompletedNutritionPlans();
        return new ResponseEntity<>(nutritionPlans, HttpStatus.OK);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<NutritionPlanResponseDto>> findNutritionPlansByClient(@PathVariable Long clientId) {
        List<NutritionPlanResponseDto> nutritionPlans = nutritionPlanService.findNutritionPlansByClient(clientId);
        return new ResponseEntity<>(nutritionPlans, HttpStatus.OK);
    }

    @GetMapping("/nutritionist/{nutritionistId}")
    public ResponseEntity<List<NutritionPlanResponseDto>> findNutritionPlansByNutritionist(@PathVariable Long nutritionistId) {
        List<NutritionPlanResponseDto> nutritionPlans = nutritionPlanService.findNutritionPlansByNutritionist(nutritionistId);
        return new ResponseEntity<>(nutritionPlans, HttpStatus.OK);
    }
}
