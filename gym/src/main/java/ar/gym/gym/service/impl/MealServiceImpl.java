package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.MealRequestDto;
import ar.gym.gym.dto.response.MealResponseDto;
import ar.gym.gym.mapper.MealMapper;
import ar.gym.gym.model.Food;
import ar.gym.gym.model.Meal;
import ar.gym.gym.model.MealDetail;
import ar.gym.gym.repository.FoodRepository;
import ar.gym.gym.repository.MealDetailRepository;
import ar.gym.gym.repository.MealRepository;
import ar.gym.gym.repository.NutritionLogRepository;
import ar.gym.gym.service.MealService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    private final MealDetailRepository mealDetailRepository;


    public MealServiceImpl(MealRepository mealRepository,
                           FoodRepository foodRepository,
                           NutritionLogRepository nutritionLogRepository,
                           MealDetailRepository mealDetailRepository,
                           MealMapper mealMapper) {
        this.mealRepository = mealRepository;
        this.foodRepository = foodRepository;
        this.nutritionLogRepository = nutritionLogRepository;
        this.mealDetailRepository = mealDetailRepository;
        this.mealMapper = mealMapper;
    }

    @Override
    public MealResponseDto createMeal(MealRequestDto mealRequestDto) {
        Meal newMeal = mealMapper.convertToEntity(mealRequestDto);
        Meal savedMeal = mealRepository.save(newMeal);
        return mealMapper.convertToDto(savedMeal);
    }

    @Override
    public MealResponseDto addFoodToMeal(Long mealId, String foodName) {
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new EntityNotFoundException("Meal not found with id: " + mealId));

        Food food = foodRepository.findByName(foodName)
                .orElseThrow(() -> new EntityNotFoundException("Food not found with name: " + foodName));

        MealDetail mealDetail = MealDetail.builder()
                .meal(meal)
                .food(food)
                .quantity(0) // definir si cambiar de lugar esta variable o
                .measureUnit("unit")
                .build();

        mealDetailRepository.save(mealDetail);
        meal.getMealDetails().add(mealDetail);
        Meal updatedMeal = mealRepository.save(meal);

        return mealMapper.convertToDto(updatedMeal);
    }


    @Override
    public MealResponseDto updateMeal(Long mealId, MealRequestDto mealRequestDto) {
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new EntityNotFoundException("Meal not found with id: " + mealId));
        if (meal != null) {
            meal.setMealTime(mealRequestDto.getMealTime());
            meal.setCompleted(mealRequestDto.getCompleted());
        }
        return mealMapper.convertToDto(meal);
    }

    @Override
    public void deleteMeal(Long id) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meal not found with id: " + id));
        mealRepository.delete(meal);
    }


    @Override
    public List<MealResponseDto> findCompletedMeals() {
        return List.of();
    }

    @Override
    public List<MealResponseDto> findAllMeals() {
        List<Meal> meals = mealRepository.findAllWithDetails();
        return meals.stream()
                .map(mealMapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MealResponseDto> findMealById(Long id) {
        return Optional.empty();
    }


}