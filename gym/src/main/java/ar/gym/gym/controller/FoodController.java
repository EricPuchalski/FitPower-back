package ar.gym.gym.controller;

import ar.gym.gym.dto.request.FoodRequestDto;
import ar.gym.gym.dto.response.FoodResponseDto;
import ar.gym.gym.service.FoodService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/foods")
public class FoodController {

    private final FoodService foodService;

    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @PostMapping
    public ResponseEntity<FoodResponseDto> createFood(@Valid @RequestBody FoodRequestDto foodRequestDto) {
        FoodResponseDto createdFood = foodService.createFood(foodRequestDto);
        return new ResponseEntity<>(createdFood, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FoodResponseDto>> findAllFoods() {
        List<FoodResponseDto> foods = foodService.findAllFoods();
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodResponseDto> findFoodById(@PathVariable Long id) {
        Optional<FoodResponseDto> food = foodService.findFoodById(id);
        return food.map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search")
    public ResponseEntity<List<FoodResponseDto>> searchFoodsByName(@RequestParam String name) {
        List<FoodResponseDto> foods = foodService.findFoodsByName(name);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    @GetMapping("/category")
    public ResponseEntity<List<FoodResponseDto>> findFoodsByCategory(@RequestParam String category) {
        List<FoodResponseDto> foods = foodService.findFoodsByCategory(category);
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodResponseDto> updateFood(@PathVariable Long id, @Valid @RequestBody FoodRequestDto foodRequestDto) {
        FoodResponseDto updatedFood = foodService.updateFood(id, foodRequestDto);
        return new ResponseEntity<>(updatedFood, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        foodService.deleteFood(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<FoodResponseDto>> findCompletedFoods() {
        List<FoodResponseDto> foods = foodService.findCompletedFoods();
        return new ResponseEntity<>(foods, HttpStatus.OK);
    }
}
