package ar.gym.gym.mapper;

import ar.gym.gym.dto.response.NotificationResponseDto;
import ar.gym.gym.model.Notification;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class NotificationMapper {

    private final ModelMapper modelMapper;


    public NotificationMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public NotificationResponseDto entityToDto(Notification notification){
        return modelMapper.map(notification, NotificationResponseDto.class);
    }

}