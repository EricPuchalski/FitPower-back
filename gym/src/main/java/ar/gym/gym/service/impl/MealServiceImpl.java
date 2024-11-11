package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.MealRequestDto;
import ar.gym.gym.dto.response.MealResponseDto;
import ar.gym.gym.mapper.MealMapper;
import ar.gym.gym.model.Food;
import ar.gym.gym.model.Meal;
import ar.gym.gym.repository.FoodRepository;
import ar.gym.gym.repository.MealRepository;
import ar.gym.gym.service.MealService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MealServiceImpl implements MealService {

    private final Logger logger = LoggerFactory.getLogger(MealServiceImpl.class);
    private final MealRepository mealRepository;
    private final FoodRepository foodRepository;
    private final MealMapper mealMapper;

    public MealServiceImpl(MealRepository mealRepository, FoodRepository foodRepository, MealMapper mealMapper) {
        this.mealRepository = mealRepository;
        this.foodRepository = foodRepository;
        this.mealMapper = mealMapper;
    }

    @Override
    public MealResponseDto createMeal(MealRequestDto mealRequestDto, int quantity) {
        logger.info("Entering createMeal method with meal data: {}", mealRequestDto);
        try {
            Meal meal = mealMapper.convertToEntity(mealRequestDto);

            // Inicializar el mapa de alimentos si es null
            if (meal.getFoods() == null) {
                meal.setFoods(new HashMap<>());
            }

            // Buscar el alimento por nombre y agregarlo al mapa de alimentos
            Food food = foodRepository.findByName(mealRequestDto.getFoodName())
                    .orElseThrow(() -> new EntityNotFoundException("Food not found with name: " + mealRequestDto.getFoodName()));
            meal.getFoods().put(food, quantity);

            Meal savedMeal = mealRepository.save(meal);
            MealResponseDto response = mealMapper.convertToDto(savedMeal);

            logger.info("Exiting createMeal method with response: {}", response);
            return response;
        } catch (Exception e) {
            logger.error("Unexpected error creating meal: {}", e.getMessage());
            throw new RuntimeException("Unexpected error creating meal", e);
        }
    }

    @Override
    public MealResponseDto updateMeal(Long id, MealRequestDto mealRequestDto) {
        logger.info("Entering updateMeal method with ID: {} and update data: {}", id, mealRequestDto);
        try {
            Meal existingMeal = mealRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Meal not found with ID: " + id));

            if (mealRequestDto.getMealTime() != null) {
                existingMeal.setMealTime(mealRequestDto.getMealTime());
            }
            existingMeal.setMeasureUnit(mealRequestDto.getMeasureUnit());
            existingMeal.setCompleted(mealRequestDto.isCompleted());

            // Actualizar el mapa de alimentos
            if (mealRequestDto.getFoods() != null) {
                existingMeal.setFoods(mealRequestDto.getFoods());
            }

            Meal updatedMeal = mealRepository.save(existingMeal);
            MealResponseDto response = mealMapper.convertToDto(updatedMeal);
            logger.info("Exiting updateMeal method with updated meal: {}", response);
            return response;
        } catch (EntityNotFoundException e) {
            logger.error("Error updating meal: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error updating meal: {}", e.getMessage());
            throw new RuntimeException("Unexpected error updating meal", e);
        }
    }

    @Override
    public void deleteMeal(Long id) {
        logger.info("Entering deleteMeal method with ID: {}", id);
        try {
            Meal meal = mealRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Meal not found with ID: " + id));

            mealRepository.delete(meal);
            logger.info("Exiting deleteMeal method with meal deleted with ID: {}", id);
        } catch (EntityNotFoundException e) {
            logger.error("Error deleting meal: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error deleting meal: {}", e.getMessage());
            throw new RuntimeException("Unexpected error deleting meal", e);
        }
    }

    @Override
    public Optional<MealResponseDto> findMealById(Long id) {
        logger.info("Entering findMealById method with ID: {}", id);
        try {
            Optional<Meal> meal = mealRepository.findById(id);
            if (meal.isEmpty()) {
                throw new EntityNotFoundException("Meal not found with ID: " + id);
            }
            MealResponseDto response = mealMapper.convertToDto(meal.get());
            logger.info("Exiting findMealById method with response: {}", response);
            return Optional.of(response);
        } catch (EntityNotFoundException e) {
            logger.error("Error finding meal: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error finding meal: {}", e.getMessage());
            throw new RuntimeException("Unexpected error finding meal", e);
        }
    }

    @Override
    public List<MealResponseDto> findCompletedMeals() {
        logger.info("Entering findCompletedMeals method");
        try {
            List<Meal> meals = mealRepository.findByCompleted(true);

            List<MealResponseDto> response = meals.stream()
                    .map(mealMapper::convertToDto)
                    .collect(Collectors.toList());

            logger.info("Exiting findCompletedMeals method with completed meals found: {}", response.size());
            return response;
        } catch (Exception e) {
            logger.error("Unexpected error searching completed meals: {}", e.getMessage());
            throw new EntityNotFoundException("Unexpected error searching completed meals", e);
        }
    }

    @Override
    public List<MealResponseDto> findAllMeals() {
        logger.info("Entering findAllMeals method");
        try {
            List<Meal> meals = mealRepository.findAll();

            List<MealResponseDto> response = meals.stream()
                    .map(mealMapper::convertToDto)
                    .collect(Collectors.toList());
            logger.info("Exiting findAllMeals method with total meals found: {}", response.size());
            return response;
        } catch (Exception e) {
            logger.error("Unexpected error fetching all meals: {}", e.getMessage());
            throw new RuntimeException("Unexpected error fetching all meals", e);
        }
    }

    @Override
    public MealResponseDto addFoodToMeal(Long mealId, Long foodId) {
        logger.info("Entering addFoodToMeal method with meal ID: {} and food ID: {}", mealId, foodId);
        try {
            Meal meal = mealRepository.findById(mealId)
                    .orElseThrow(() -> new EntityNotFoundException("Meal not found with ID: " + mealId));

            Food food = foodRepository.findById(foodId)
                    .orElseThrow(() -> new EntityNotFoundException("Food not found with ID: " + foodId));

            // Agregar el alimento al mapa de alimentos de la comida
            meal.getFoods().put(food, 1); // Asignar una cantidad de 1 por defecto

            Meal updatedMeal = mealRepository.save(meal);
            MealResponseDto response = mealMapper.convertToDto(updatedMeal);

            logger.info("Exiting addFoodToMeal method with response: {}", response);
            return response;
        } catch (EntityNotFoundException e) {
            logger.error("Error adding food to meal: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error adding food to meal: {}", e.getMessage());
            throw new RuntimeException("Unexpected error adding food to meal", e);
        }
    }
}