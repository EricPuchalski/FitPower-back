package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.MealRequestDto;
import ar.gym.gym.dto.response.MealResponseDto;
import ar.gym.gym.mapper.MealMapper;
import ar.gym.gym.model.Food;
import ar.gym.gym.model.Meal;
import ar.gym.gym.model.MealDetail;
import ar.gym.gym.repository.FoodRepository;
import ar.gym.gym.repository.MealItemRepository;
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
    public MealResponseDto createMeal(MealRequestDto mealRequestDto) {
        Meal newMeal = mealMapper.convertToEntity(mealRequestDto);
        Meal savedMeal = mealRepository.save(newMeal);
        return mealMapper.convertToDto(savedMeal);
    }

    @Override
    public MealResponseDto addFoodToMeal(Long mealId, String foodName) {
        // Buscar la comida por su ID
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new EntityNotFoundException("Meal not found with id: " + mealId));

        // Buscar el alimento por su nombre
        Food food = foodRepository.findByName(foodName)
                .orElseThrow(() -> new EntityNotFoundException("Food not found with name: " + foodName));

        // Crear un nuevo detalle de comida
        MealDetail mealDetail = new MealDetail();
        mealDetail.setMeal(meal);
        mealDetail.setFood(food);
        mealDetail.setQuantity(1); // Puedes ajustar la cantidad según tus necesidades
        mealDetail.setMeasureUnit("unit"); // Puedes ajustar la unidad de medida según tus necesidades

        // Guardar el nuevo detalle de comida
        mealItemRepository.save(mealDetail);

        // Actualizar la comida con el nuevo detalle de comida
        meal.getMealDetails().add(mealDetail);

        // Guardar la comida actualizada
        Meal updatedMeal = mealRepository.save(meal);

        // Convertir y retornar la respuesta
        return mealMapper.convertToDto(updatedMeal);
    }

    /**
     * d
     */
    /*  @Override
      @Transactional
      public MealResponseDto createMeal(MealRequestDto mealRequestDto) {
          // Crear la nueva comida
          Meal meal = new Meal();
          meal.setMealTime(mealRequestDto.getMealTime());
          meal.setNutritionLog(null); // Establecer NutritionLog a null
          meal.setCompleted(false);

          // Lista para almacenar los items de la comida
          List<MealDetail> mealDetails = new ArrayList<>();

          // Procesar cada item de comida solicitado
          for (MealItemRequestDto itemDto : mealRequestDto.getMealItems()) {
              // Buscar el alimento por nombre
              Food food = foodRepository.findByName(itemDto.getFoodName())
                      .orElseThrow(() -> new EntityNotFoundException(
                              "Food not found with name: " + itemDto.getFoodName()));

              // Crear el MealItem
              MealDetail mealDetail = new MealDetail();
              mealDetail.setMeal(meal);
              mealDetail.setFood(food);
              mealDetail.setQuantity(itemDto.getQuantity());
              mealDetail.setMeasureUnit(itemDto.getMeasureUnit());

              mealDetails.add(mealDetail);
          }

          meal.setMealDetails(mealDetails);

          // Guardar la comida
          Meal savedMeal = mealRepository.save(meal);

          // Convertir y retornar la respuesta
          return mealMapper.convertToDto(savedMeal);
      }

     */
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