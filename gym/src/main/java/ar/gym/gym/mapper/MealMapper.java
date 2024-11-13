package ar.gym.gym.mapper;


import ar.gym.gym.dto.request.MealRequestDto;
import ar.gym.gym.dto.response.MealResponseDto;
import ar.gym.gym.model.Meal;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealMapper {

    private final ModelMapper modelMapper;

    public MealMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public MealResponseDto convertToDto(Meal meal) {
        return modelMapper.map(meal, MealResponseDto.class);
    }

    public Meal convertToEntity(MealRequestDto mealRequestDto) {
        return modelMapper.map(mealRequestDto, Meal.class);
    }

    public List<MealResponseDto> convertToDtoList(List<Meal> meals) {
        return meals.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
