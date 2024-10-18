package ar.gym.gym.mapper;

import ar.gym.gym.dto.request.ExerciseRequestDto;
import ar.gym.gym.dto.request.GymRequestDto;
import ar.gym.gym.dto.response.ExerciseResponseDto;
import ar.gym.gym.dto.response.GymResponseDto;
import ar.gym.gym.model.Exercise;
import ar.gym.gym.model.Gym;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ExerciseMapper {

    private final ModelMapper modelMapper;


    public ExerciseMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ExerciseResponseDto entityToDto(Exercise exercise){
        return modelMapper.map(exercise, ExerciseResponseDto.class);
    }

    public Exercise dtoToEntity(ExerciseRequestDto exerciseRequestDto){
        return modelMapper.map(exerciseRequestDto, Exercise.class);
    }
}
