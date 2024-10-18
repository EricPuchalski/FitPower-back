package ar.gym.gym.controller;

import com.itec.FitFlowApp.dto.request.NutritionistRequestDto;
import com.itec.FitFlowApp.dto.response.ClientResponseDto;
import com.itec.FitFlowApp.dto.response.NutritionistResponseDto;
import com.itec.FitFlowApp.model.service.NutritionistService;
import com.itec.FitFlowApp.util.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/fit_flow/nutritionist")
public class NutritionistController implements Controller<NutritionistResponseDto, NutritionistRequestDto> {
    @Autowired
    private NutritionistService nutritionistService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<NutritionistResponseDto> create(NutritionistRequestDto nutritionistRequestDto) {
        NutritionistResponseDto createdNutritionist = nutritionistService.create(nutritionistRequestDto);
        return new ResponseEntity<>(createdNutritionist, HttpStatus.CREATED);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<NutritionistResponseDto>> findAll() {
        List<NutritionistResponseDto> nutritionist = nutritionistService.findAll();
        return ResponseEntity.ok(nutritionist);
    }

    @Override
    @GetMapping("/{dni}")
    public ResponseEntity<NutritionistResponseDto> findById(String id) {
        NutritionistResponseDto nutritionist = nutritionistService.findById(id);
        return ResponseEntity.ok(nutritionist);
    }

    @Override
    @PutMapping("/{dni}")
    public ResponseEntity<NutritionistResponseDto> update(NutritionistRequestDto nutritionistRequestDto) {
        NutritionistResponseDto updatedNutritionist = nutritionistService.update(nutritionistRequestDto);
        return ResponseEntity.ok(updatedNutritionist);
    }

    @Override
    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> delete(String id) {
        nutritionistService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{dni}")
    public ResponseEntity<List<ClientResponseDto>> getClients(@PathVariable String dni){
        List<ClientResponseDto>nutritionistClients = nutritionistService.getClients(dni);
        return ResponseEntity.ok(nutritionistClients);
    }

}
