package ar.gym.gym.mapper;

import ar.gym.gym.dto.request.ClientStatusRequestDto;
import ar.gym.gym.dto.response.ClientStatusResponseDto;
import ar.gym.gym.model.ClientStatus;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ClientStatusMapper {

    private final ModelMapper modelMapper;


    public ClientStatusMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ClientStatusResponseDto entityToDto(ClientStatus clientStatus){
        return modelMapper.map(clientStatus, ClientStatusResponseDto.class);
    }

    public ClientStatus dtoToEntity(ClientStatusRequestDto clientStatusRequestDto){
        return modelMapper.map(clientStatusRequestDto, ClientStatus.class);
    }
}
