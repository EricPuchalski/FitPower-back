package ar.gym.gym.mapper;

import ar.gym.gym.dto.response.NotificationResponseDto;
import ar.gym.gym.dto.response.PerfomanceResponseDto;

import ar.gym.gym.model.Performance;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class PerfomanceMapper {
    private final ModelMapper modelMapper;


    public PerfomanceMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public PerfomanceResponseDto entityToDto(Performance performance){
        return modelMapper.map(performance, PerfomanceResponseDto.class);
    }
}
