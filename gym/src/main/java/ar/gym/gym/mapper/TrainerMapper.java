package ar.gym.gym.mapper;

import ar.gym.gym.dto.request.TrainerRequestDto;
import ar.gym.gym.dto.response.TrainerResponseDto;
import ar.gym.gym.model.Trainer;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class TrainerMapper {
    private final ModelMapper modelMapper;


    public TrainerMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public TrainerResponseDto entityToDto(Trainer trainer){
        return modelMapper.map(trainer, TrainerResponseDto.class);
    }

    public Trainer dtoToEntity(TrainerRequestDto trainerRequestDto){
        return modelMapper.map(trainerRequestDto, Trainer.class);
    }
}
