package ar.gym.gym.controller;

import ar.gym.gym.dto.request.FoodRequestDto;
import ar.gym.gym.dto.response.FoodResponseDto;
import ar.gym.gym.service.FoodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PostMapping
    public ResponseEntity<FoodResponseDto> createFood(@RequestBody FoodRequestDto foodRequestDto) {
        FoodResponseDto response = foodService.createFood(foodRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FoodResponseDto>> findAllFoods() {
        List<FoodResponseDto> response = foodService.findAllFoods();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodResponseDto> findFoodById(@PathVariable Long id) {
        Optional<FoodResponseDto> response = foodService.findFoodById(id);
        return response.map(foodResponseDto -> new ResponseEntity<>(foodResponseDto, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search")
    public ResponseEntity<List<FoodResponseDto>> findFoodsByName(@RequestParam String name) {
        List<FoodResponseDto> response = foodService.findFoodsByName(name);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<FoodResponseDto>> findFoodsByCategory(@PathVariable String category) {
        List<FoodResponseDto> response = foodService.findFoodsByCategory(category);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodResponseDto> updateFood(@PathVariable Long id, @RequestBody FoodRequestDto foodRequestDto) {
        FoodResponseDto response = foodService.updateFood(id, foodRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<FoodResponseDto>> findCompletedFoods() {
        List<FoodResponseDto> response = foodService.findCompletedFoods();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
