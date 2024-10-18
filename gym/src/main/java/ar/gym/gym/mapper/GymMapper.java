package ar.gym.gym.mapper;

import ar.gym.gym.dto.request.ClientRequestDto;
import ar.gym.gym.dto.request.GymRequestDto;
import ar.gym.gym.dto.response.ClientResponseDto;
import ar.gym.gym.dto.response.GymResponseDto;
import ar.gym.gym.model.Client;
import ar.gym.gym.model.Gym;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class GymMapper {

    private final ModelMapper modelMapper;


    public GymMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public GymResponseDto entityToDto(Gym gym){
        return modelMapper.map(gym, GymResponseDto.class);
    }

    public Gym dtoToEntity(GymRequestDto gymRequestDto){
        return modelMapper.map(gymRequestDto, Gym.class);
    }
}
