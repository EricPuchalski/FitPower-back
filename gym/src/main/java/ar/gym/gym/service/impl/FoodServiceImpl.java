package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.FoodRequestDto;
import ar.gym.gym.dto.response.FoodResponseDto;
import ar.gym.gym.mapper.FoodMapper;
import ar.gym.gym.model.Food;
import ar.gym.gym.repository.FoodRepository;
import ar.gym.gym.service.FoodService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodServiceImpl implements FoodService {

    private final Logger logger = LoggerFactory.getLogger(FoodServiceImpl.class);
    private final FoodRepository foodRepository;
    private final FoodMapper foodMapper;

    public FoodServiceImpl(FoodRepository foodRepository, FoodMapper foodMapper) {
        this.foodRepository = foodRepository;
        this.foodMapper = foodMapper;
    }

    @Override
    public FoodResponseDto createFood(FoodRequestDto foodRequestDto) {
        logger.info("Entering createFood method with food data: {}", foodRequestDto);
        try {
            if (foodRepository.findByName(foodRequestDto.getName()).isPresent()) {
                throw new EntityExistsException("Food with name " + foodRequestDto.getName() + " already exists");
            }
            Food food = foodMapper.convertToEntity(foodRequestDto);
            Food savedFood = foodRepository.save(food);

            FoodResponseDto response = foodMapper.convertToDto(savedFood);

            logger.info("Exiting createFood method with response: {}", response);
            return response;
        } catch (EntityExistsException e) {
            logger.error("Error creating food: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error creating food: {}", e.getMessage());
            throw new RuntimeException("Unexpected error creating food", e);
        }
    }

    @Override
    public List<FoodResponseDto> findAllFoods() {
        logger.info("Entering findAllFoods method");
        try {
            List<Food> foods = foodRepository.findAll();

            List<FoodResponseDto> response = foods.stream()
                    .map(foodMapper::convertToDto)
                    .collect(Collectors.toList());
            logger.info("Exiting findAllFoods method with total foods found: {}", response.size());
            return response;
        } catch (Exception e) {
            logger.error("Unexpected error fetching all foods: {}", e.getMessage());
            throw new RuntimeException("Unexpected error fetching all foods", e);
        }
    }

    @Override
    public Optional<FoodResponseDto> findFoodById(Long id) {
        logger.info("Entering findFoodById method with ID: {}", id);
        try {
            Optional<Food> food = foodRepository.findById(id);
            if (food.isEmpty()) {
                throw new EntityNotFoundException("Food not found with ID: " + id);
            }
            FoodResponseDto response = foodMapper.convertToDto(food.get());
            logger.info("Exiting findFoodById method with response: {}", response);
            return Optional.of(response);
        } catch (EntityNotFoundException e) {
            logger.error("Error finding food: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error finding food: {}", e.getMessage());
            throw new RuntimeException("Unexpected error finding food", e);
        }
    }

    @Override
    public List<FoodResponseDto> findFoodsByName(String name) {
        logger.info("Entering findFoodsByName method with name: {}", name);
        try {
            List<Food> foods = foodRepository.findByNameContainingIgnoreCase(name);

            List<FoodResponseDto> response = foods.stream()
                    .map(foodMapper::convertToDto)
                    .collect(Collectors.toList());
            logger.info("Exiting findFoodsByName method with foods found: {}", response.size());
            return response;
        } catch (Exception e) {
            logger.error("Unexpected error searching foods by name: {}", e.getMessage());
            throw new RuntimeException("Unexpected error searching foods by name", e);
        }
    }

    @Override
    public List<FoodResponseDto> findFoodsByCategory(String category) {
        logger.info("Entering findFoodsByCategory method with category: {}", category);
        try {
            List<Food> foods = foodRepository.findByCategory(category);

            List<FoodResponseDto> response = foods.stream()
                    .map(foodMapper::convertToDto)
                    .collect(Collectors.toList());
            logger.info("Exiting findFoodsByCategory method with foods found: {}", response.size());
            return response;
        } catch (Exception e) {
            logger.error("Unexpected error searching foods by category: {}", e.getMessage());
            throw new RuntimeException("Unexpected error searching foods by category", e);
        }
    }

    @Override
    public FoodResponseDto updateFood(Long id, FoodRequestDto foodRequestDto) {
        logger.info("Entering updateFood method with ID: {} and update data: {}", id, foodRequestDto);
        try {
            Food existingFood = foodRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Food not found with ID: " + id));
            if (foodRequestDto.getName() != null) {
                existingFood.setName(foodRequestDto.getName());
            }
            if (foodRequestDto.getCalories() != null) {
                existingFood.setCalories(foodRequestDto.getCalories());
            }
            if (foodRequestDto.getCategory() != null) {
                existingFood.setCategory(foodRequestDto.getCategory());
            }
            if (foodRequestDto.getMeasureUnit() != null) {
                existingFood.setMeasureUnit(foodRequestDto.getMeasureUnit());
            }
            Food updatedFood = foodRepository.save(existingFood);
            FoodResponseDto response = foodMapper.convertToDto(updatedFood);
            logger.info("Exiting updateFood method with updated food: {}", response);
            return response;
        } catch (EntityNotFoundException e) {
            logger.error("Error updating food: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error updating food: {}", e.getMessage());
            throw new RuntimeException("Unexpected error updating food", e);
        }
    }

    @Override
    public void deleteFood(Long id) {
        logger.info("Entering deleteFood method with ID: {}", id);
        try {
            Food food = foodRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Food not found with ID: " + id));

            foodRepository.delete(food);
            logger.info("Exiting deleteFood method with food deleted with ID: {}", id);
        } catch (EntityNotFoundException e) {
            logger.error("Error deleting food: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error deleting food: {}", e.getMessage());
            throw new RuntimeException("Unexpected error deleting food", e);
        }
    }

    @Override
    public List<FoodResponseDto> findCompletedFoods() {
        logger.info("Entering findCompletedFoods method");
        try {
            List<Food> foods = foodRepository.findByCompleted(true);

            List<FoodResponseDto> response = foods.stream()
                    .map(foodMapper::convertToDto)
                    .collect(Collectors.toList());
            logger.info("Exiting findCompletedFoods method with completed foods found: {}", response.size());
            return response;
        } catch (Exception e) {
            logger.error("Unexpected error searching completed foods: {}", e.getMessage());
            throw new RuntimeException("Unexpected error searching completed foods", e);
        }
    }
}
