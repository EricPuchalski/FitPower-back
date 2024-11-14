package ar.gym.gym.mapper;

import ar.gym.gym.dto.request.TrainingPlanRequestDto;
import ar.gym.gym.dto.response.TrainingPlanResponseDto;
import ar.gym.gym.model.TrainingPlan;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class TrainingPlanMapper {

    private final ModelMapper modelMapper;

    public TrainingPlanMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Método para convertir la entidad TrainingPlan a TrainingPlanResponseDto
    public TrainingPlanResponseDto entityToDto(TrainingPlan trainingPlan) {
        return modelMapper.map(trainingPlan, TrainingPlanResponseDto.class);
    }

    // Método para convertir TrainingPlanRequestDto a la entidad TrainingPlan
    public TrainingPlan dtoToEntity(TrainingPlanRequestDto trainingPlanRequestDto) {
        return modelMapper.map(trainingPlanRequestDto, TrainingPlan.class);
    }
}
