package ar.gym.gym.mapper;

import ar.gym.gym.dto.request.ClientRequestDto;
import ar.gym.gym.dto.response.ClientResponseDto;
import ar.gym.gym.model.Client;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ClientMapper {
    private final ModelMapper modelMapper;


    public ClientMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ClientResponseDto entityToDto(Client client){
        return modelMapper.map(client, ClientResponseDto.class);
    }

    public Client dtoToEntity(ClientRequestDto clientRequestDto){
        return modelMapper.map(clientRequestDto, Client.class);
    }
}
