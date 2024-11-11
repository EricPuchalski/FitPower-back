package ar.gym.gym.mapper;

import ar.gym.gym.dto.request.NutritionLogRequestDto;
import ar.gym.gym.dto.response.NutritionLogResponseDto;
import ar.gym.gym.model.NutritionLog;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class NutritionLogMapper {

    private final ModelMapper modelMapper;

    public NutritionLogMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public NutritionLogResponseDto convertToDto(NutritionLog nutritionLog) {
        return modelMapper.map(nutritionLog, NutritionLogResponseDto.class);
    }

    public NutritionLog convertToEntity(NutritionLogRequestDto nutritionLogRequestDto) {
        return modelMapper.map(nutritionLogRequestDto, NutritionLog.class);
    }
}
