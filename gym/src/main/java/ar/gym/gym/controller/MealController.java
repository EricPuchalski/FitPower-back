package ar.gym.gym.controller;

import ar.gym.gym.dto.request.MealRequestDto;
import ar.gym.gym.dto.response.MealResponseDto;
import ar.gym.gym.service.MealService;
import jakarta.persistence.EntityNotFoundException;
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
        try {
            MealResponseDto createdMeal = mealService.createMeal(mealRequestDto);
            return new ResponseEntity<>(createdMeal, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
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


    @PostMapping("/{mealId}/addFood")
    public ResponseEntity<MealResponseDto> addFoodToMeal(@PathVariable Long mealId, @RequestParam String foodName) {
        try {
            MealResponseDto updatedMeal = mealService.addFoodToMeal(mealId, foodName);
            return new ResponseEntity<>(updatedMeal, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
/*
    @PostMapping("/{mealId}/foods/{foodId}")
    public ResponseEntity<MealResponseDto> addFoodToMeal(@PathVariable Long mealId, @RequestBody Long foodId) {
        MealResponseDto updatedMeal = mealService.addFoodToMeal(mealId, foodId);
        return new ResponseEntity<>(updatedMeal, HttpStatus.OK);
    }

 */
}
