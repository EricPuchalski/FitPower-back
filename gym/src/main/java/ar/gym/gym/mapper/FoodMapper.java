package ar.gym.gym.mapper;

import ar.gym.gym.dto.request.FoodRequestDto;
import ar.gym.gym.dto.response.FoodResponseDto;
import ar.gym.gym.model.Food;
import org.modelmapper.ModelMapper;

public class FoodMapper {

    private final ModelMapper modelMapper;

    public FoodMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public FoodResponseDto convertToDto(Food food) {
        return modelMapper.map(food, FoodResponseDto.class);
    }

    public Food convertToEntity(FoodRequestDto exerciseRequestDto) {
        return modelMapper.map(exerciseRequestDto, Food.class);
    }

}
