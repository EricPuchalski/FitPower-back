package ar.gym.gym.mapper;

import ar.gym.gym.dto.request.GymRequestDto;
import ar.gym.gym.dto.request.NutritionistRequestDto;
import ar.gym.gym.dto.response.GymResponseDto;
import ar.gym.gym.dto.response.NutritionistResponseDto;
import ar.gym.gym.model.Gym;
import ar.gym.gym.model.Nutritionist;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class NutritionistMapper {
    private final ModelMapper modelMapper;


    public NutritionistMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public NutritionistResponseDto entityToDto(Nutritionist nutritionist){
        return modelMapper.map(nutritionist, NutritionistResponseDto.class);
    }

    public Nutritionist dtoToEntity(NutritionistRequestDto nutritionistRequestDto){
        return modelMapper.map(nutritionistRequestDto, Nutritionist.class);
    }
}
