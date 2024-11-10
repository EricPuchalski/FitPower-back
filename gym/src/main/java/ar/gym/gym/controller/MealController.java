package ar.gym.gym.controller;

import ar.gym.gym.dto.request.MealRequestDto;
import ar.gym.gym.dto.response.MealResponseDto;
import ar.gym.gym.service.MealService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/meals")
public class MealController {

    private final MealService mealService;

    @Autowired
    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping
    public ResponseEntity<MealResponseDto> createMeal(@Valid @RequestBody MealRequestDto mealRequestDto) {
        MealResponseDto createdMeal = mealService.createMeal(mealRequestDto);
        return new ResponseEntity<>(createdMeal, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MealResponseDto>> getAllMeals() {
        List<MealResponseDto> meals = mealService.findAllMeals();
        return new ResponseEntity<>(meals, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MealResponseDto> getMealById(@PathVariable Long id) {
        Optional<MealResponseDto> meal = mealService.findMealById(id);
        return meal.map(response -> new ResponseEntity<>(response, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MealResponseDto> updateMeal(@PathVariable Long id, @Valid @RequestBody MealRequestDto mealRequestDto) {
        MealResponseDto updatedMeal = mealService.updateMeal(id, mealRequestDto);
        return new ResponseEntity<>(updatedMeal, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeal(@PathVariable Long id) {
        mealService.deleteMeal(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/completed")
    public ResponseEntity<List<MealResponseDto>> getCompletedMeals() {
        List<MealResponseDto> meals = mealService.findCompletedMeals();
        return new ResponseEntity<>(meals, HttpStatus.OK);
    }
}