package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.MealItemRequestDto;
import ar.gym.gym.dto.request.MealRequestDto;
import ar.gym.gym.dto.response.MealResponseDto;
import ar.gym.gym.mapper.MealMapper;
import ar.gym.gym.model.Food;
import ar.gym.gym.model.Meal;
import ar.gym.gym.model.MealItem;
import ar.gym.gym.model.NutritionLog;
import ar.gym.gym.repository.FoodRepository;
import ar.gym.gym.repository.MealItemRepository;
import ar.gym.gym.repository.MealRepository;
import ar.gym.gym.repository.NutritionLogRepository;
import ar.gym.gym.service.MealService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MealServiceImpl implements MealService {

    private final Logger logger = LoggerFactory.getLogger(MealServiceImpl.class);
    private final MealRepository mealRepository;
    private final FoodRepository foodRepository;
    private final NutritionLogRepository nutritionLogRepository;
    private final MealMapper mealMapper;
    private final MealItemRepository mealItemRepository;


    public MealServiceImpl(MealRepository mealRepository,
                           FoodRepository foodRepository,
                           NutritionLogRepository nutritionLogRepository,
                           MealItemRepository mealItemRepository,
                           MealMapper mealMapper) {
        this.mealRepository = mealRepository;
        this.foodRepository = foodRepository;
        this.nutritionLogRepository = nutritionLogRepository;
        this.mealItemRepository = mealItemRepository;
        this.mealMapper = mealMapper;
    }

    @Override
    @Transactional
    public MealResponseDto createMeal(MealRequestDto mealRequestDto) {
        // Crear la nueva comida
        Meal meal = new Meal();
        meal.setMealTime(mealRequestDto.getMealTime());
        meal.setNutritionLog(null); // Establecer NutritionLog a null
        meal.setCompleted(false);

        // Lista para almacenar los items de la comida
        List<MealItem> mealItems = new ArrayList<>();

        // Procesar cada item de comida solicitado
        for (MealItemRequestDto itemDto : mealRequestDto.getMealItems()) {
            // Buscar el alimento por nombre
            Food food = foodRepository.findByName(itemDto.getFoodName())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Food not found with name: " + itemDto.getFoodName()));

            // Crear el MealItem
            MealItem mealItem = new MealItem();
            mealItem.setMeal(meal);
            mealItem.setFood(food);
            mealItem.setQuantity(itemDto.getQuantity());
            mealItem.setMeasureUnit(itemDto.getMeasureUnit());

            mealItems.add(mealItem);
        }

        meal.setMealItems(mealItems);

        // Guardar la comida
        Meal savedMeal = mealRepository.save(meal);

        // Convertir y retornar la respuesta
        return mealMapper.convertToDto(savedMeal);
    }

    @Override
    public MealResponseDto updateMeal(Long id, MealRequestDto mealRequestDto) {
        return null;
    }

    @Override
    public void deleteMeal(Long id) {

    }

    @Override
    public List<MealResponseDto> findCompletedMeals() {
        return List.of();
    }

    @Override
    public List<MealResponseDto> findAllMeals() {
        return List.of();
    }

    @Override
    public Optional<MealResponseDto> findMealById(Long id) {
        return Optional.empty();
    }

    @Override
    public MealResponseDto addFoodToMeal(Long mealId, Long foodId) {
        return null;
    }

    /**

     d
     */

}