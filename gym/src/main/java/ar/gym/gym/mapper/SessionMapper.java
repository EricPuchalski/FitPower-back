package ar.gym.gym.mapper;

import ar.gym.gym.dto.request.NutritionistRequestDto;
import ar.gym.gym.dto.request.SessionRequestDto;
import ar.gym.gym.dto.response.NutritionistResponseDto;
import ar.gym.gym.dto.response.SessionResponseDto;
import ar.gym.gym.model.Nutritionist;
import ar.gym.gym.model.Session;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class SessionMapper {

    private final ModelMapper modelMapper;


    public SessionMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public SessionResponseDto entityToDto(Session session){
        return modelMapper.map(session, SessionResponseDto.class);
    }

    public Session dtoToEntity(SessionRequestDto sessionRequestDto){
        return modelMapper.map(sessionRequestDto, Session.class);
    }
}
