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
        logger.info("Entrando al método createFood con datos del alimento: {}", foodRequestDto);

        // Verificar si ya existe un alimento con el mismo nombre
        if (foodRepository.findByName(foodRequestDto.getName()).isPresent()) {
            throw new EntityExistsException("Ya existe un alimento con el nombre " + foodRequestDto.getName());
        }

        // Convertir DTO a entidad
        Food food = foodMapper.convertToEntity(foodRequestDto);

        // Guardar el alimento en la base de datos
        Food savedFood = foodRepository.save(food);

        // Mapear entidad a DTO de respuesta
        FoodResponseDto response = foodMapper.convertToDto(savedFood);

        logger.info("Saliendo del método createFood con respuesta: {}", response);
        return response;
    }

    @Override
    public List<FoodResponseDto> findAllFoods() {
        logger.info("Entrando al método findAllFoods");

        // Obtener todos los alimentos desde el repositorio
        List<Food> foods = foodRepository.findAll();

        // Mapear la lista de entidades a una lista de DTOs
        List<FoodResponseDto> response = foods.stream()
                .map(foodMapper::convertToDto)
                .collect(Collectors.toList());

        logger.info("Saliendo del método findAllFoods con número total de alimentos encontrados: {}", response.size());
        return response;
    }

    @Override
    public Optional<FoodResponseDto> findFoodById(Long id) {
        logger.info("Entrando al método findFoodById con ID: {}", id);

        // Buscar el alimento por su ID
        Optional<Food> food = foodRepository.findById(id);
        if (food.isEmpty()) {
            throw new EntityNotFoundException("Alimento no encontrado con el ID: " + id);
        }

        // Mapear entidad a DTO de respuesta
        FoodResponseDto response = foodMapper.convertToDto(food.get());

        logger.info("Saliendo del método findFoodById con respuesta: {}", response);
        return Optional.of(response);
    }

    @Override
    public List<FoodResponseDto> findFoodsByName(String name) {
        logger.info("Entrando al método findFoodsByName con nombre: {}", name);

        // Buscar los alimentos por nombre
        List<Food> foods = foodRepository.findByNameContainingIgnoreCase(name);

        // Mapear la lista de entidades a una lista de DTOs
        List<FoodResponseDto> response = foods.stream()
                .map(foodMapper::convertToDto)
                .collect(Collectors.toList());

        logger.info("Saliendo del método findFoodsByName con número de alimentos encontrados: {}", response.size());
        return response;
    }

    @Override
    public List<FoodResponseDto> findFoodsByCategory(String category) {
        logger.info("Entrando al método findFoodsByCategory con categoría: {}", category);

        // Buscar los alimentos por categoría
        List<Food> foods = foodRepository.findByCategory(category);

        // Mapear la lista de entidades a una lista de DTOs
        List<FoodResponseDto> response = foods.stream()
                .map(foodMapper::convertToDto)
                .collect(Collectors.toList());

        logger.info("Saliendo del método findFoodsByCategory con número de alimentos encontrados: {}", response.size());
        return response;
    }

    @Override
    public FoodResponseDto updateFood(Long id, FoodRequestDto foodRequestDto) {
        logger.info("Entrando al método updateFood con ID: {} y datos de actualización: {}", id, foodRequestDto);

        // Verificar si el alimento existe
        Food existingFood = foodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Alimento no encontrado con el ID: " + id));

        // Actualizar los datos del alimento
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
        // ... añadir más campos según se necesite

        // Guardar el alimento actualizado
        Food updatedFood = foodRepository.save(existingFood);

        // Mapear entidad a DTO de respuesta
        FoodResponseDto response = foodMapper.convertToDto(updatedFood);

        logger.info("Saliendo del método updateFood con alimento actualizado: {}", response);
        return response;
    }

    @Override
    public void deleteFood(Long id) {
        logger.info("Entrando al método deleteFood con ID: {}", id);

        // Verificar si el alimento existe
        Food food = foodRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Alimento no encontrado con el ID: " + id));

        // Eliminar el alimento
        foodRepository.delete(food);

        logger.info("Saliendo del método deleteFood con alimento eliminado con ID: {}", id);
    }

    @Override
    public List<FoodResponseDto> findCompletedFoods() {
        logger.info("Entrando al método findCompletedFoods");

        // Buscar los alimentos marcados como completados
        List<Food> foods = foodRepository.findByCompleted(true);

        // Mapear la lista de entidades a una lista de DTOs
        List<FoodResponseDto> response = foods.stream()
                .map(foodMapper::convertToDto)
                .collect(Collectors.toList());

        logger.info("Saliendo del método findCompletedFoods con número de alimentos encontrados: {}", response.size());
        return response;
    }
}
