package ar.gym.gym.service;

import ar.gym.gym.dto.request.FoodRequestDto;
import ar.gym.gym.dto.response.FoodResponseDto;
import ar.gym.gym.model.Food;

import java.util.List;
import java.util.Optional;

public interface FoodService {

    FoodResponseDto createFood(FoodRequestDto foodRequestDto);
    List<FoodResponseDto> findAllFoods();
    Optional<FoodResponseDto> findFoodById(Long id);
    List<FoodResponseDto> findFoodsByName(String name);
    List<FoodResponseDto> findFoodsByCategory(String category);
    FoodResponseDto updateFood(Long id, FoodRequestDto foodRequestDto);
    void deleteFood(Long id);
    List<FoodResponseDto> findCompletedFoods();
}
