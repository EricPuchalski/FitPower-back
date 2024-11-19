package ar.gym.gym.mapper;

import ar.gym.gym.dto.request.RoutineRequestDto;
import ar.gym.gym.dto.response.RoutineCreateResponseDto;
import ar.gym.gym.dto.response.RoutineResponseDto;
import ar.gym.gym.model.Routine;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class RoutineMapper {
    private final ModelMapper modelMapper;


    public RoutineMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public RoutineResponseDto entityToDto(Routine routine){
        return modelMapper.map(routine, RoutineResponseDto.class);
    }

    public RoutineCreateResponseDto entityToDtoCreate(Routine routine){
        return modelMapper.map(routine, RoutineCreateResponseDto.class);
    }

    public Routine dtoToEntity(RoutineRequestDto routineRequestDto){
        return modelMapper.map(routineRequestDto, Routine.class);
    }
}
