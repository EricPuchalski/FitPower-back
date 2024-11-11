package ar.gym.gym.mapper;

import ar.gym.gym.dto.request.NutritionPlanRequestDto;
import ar.gym.gym.dto.response.NutritionPlanResponseDto;
import ar.gym.gym.model.NutritionPlan;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class NutritionPlanMapper {

    private final ModelMapper modelMapper;

    public NutritionPlanMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public NutritionPlanResponseDto convertToDto(NutritionPlan nutritionPlan) {
        return modelMapper.map(nutritionPlan, NutritionPlanResponseDto.class);
    }

    public NutritionPlan convertToEntity(NutritionPlanRequestDto nutritionLogRequestDto) {
        return modelMapper.map(nutritionLogRequestDto, NutritionPlan.class);
    }
}
