package ar.gym.gym.controller;

import ar.gym.gym.dto.request.NutritionLogRequestDto;
import ar.gym.gym.dto.response.NutritionLogResponseDto;
import ar.gym.gym.service.NutritionLogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/nutrition-logs")
public class NutritionLogController {

    private final NutritionLogService nutritionLogService;

    @Autowired
    public NutritionLogController(NutritionLogService nutritionLogService) {
        this.nutritionLogService = nutritionLogService;
    }

    @PostMapping
    public ResponseEntity<NutritionLogResponseDto> createNutritionLog(@Valid @RequestBody NutritionLogRequestDto nutritionLogRequestDto) {
        NutritionLogResponseDto createdNutritionLog = nutritionLogService.createNutritionLog(nutritionLogRequestDto);
        return new ResponseEntity<>(createdNutritionLog, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<NutritionLogResponseDto>> getAllNutritionLogs() {
        List<NutritionLogResponseDto> nutritionLogs = nutritionLogService.findAllNutritionLogs();
        return new ResponseEntity<>(nutritionLogs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NutritionLogResponseDto> getNutritionLogById(@PathVariable Long id) {
        Optional<NutritionLogResponseDto> nutritionLog = nutritionLogService.findNutritionLogById(id);
        return nutritionLog.map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NutritionLogResponseDto> updateNutritionLog(@PathVariable Long id, @Valid @RequestBody NutritionLogRequestDto nutritionLogRequestDto) {
        NutritionLogResponseDto updatedNutritionLog = nutritionLogService.updateNutritionLog(id, nutritionLogRequestDto);
        return new ResponseEntity<>(updatedNutritionLog, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNutritionLog(@PathVariable Long id) {
        nutritionLogService.deleteNutritionLog(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<NutritionLogResponseDto>> getCompletedNutritionLogs() {
        List<NutritionLogResponseDto> nutritionLogs = nutritionLogService.findCompletedNutritionLogs();
        return new ResponseEntity<>(nutritionLogs, HttpStatus.OK);
    }

    @GetMapping("/date")
    public ResponseEntity<List<NutritionLogResponseDto>> getNutritionLogsByDate(@RequestParam LocalDateTime date) {
        List<NutritionLogResponseDto> nutritionLogs = nutritionLogService.findNutritionLogsByDate(date);
        return new ResponseEntity<>(nutritionLogs, HttpStatus.OK);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<NutritionLogResponseDto>> getNutritionLogsByClient(@PathVariable Long clientId) {
        List<NutritionLogResponseDto> nutritionLogs = nutritionLogService.findNutritionLogsByClient(clientId);
        return new ResponseEntity<>(nutritionLogs, HttpStatus.OK);
    }

    @GetMapping("/nutrition-plan/{nutritionPlanId}")
    public ResponseEntity<List<NutritionLogResponseDto>> getNutritionLogsByNutritionPlan(@PathVariable Long nutritionPlanId) {
        List<NutritionLogResponseDto> nutritionLogs = nutritionLogService.findNutritionLogsByNutritionPlan(nutritionPlanId);
        return new ResponseEntity<>(nutritionLogs, HttpStatus.OK);
    }
}
