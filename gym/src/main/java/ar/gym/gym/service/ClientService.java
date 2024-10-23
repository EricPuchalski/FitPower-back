package ar.gym.gym.service;

import ar.gym.gym.dto.request.ClientRequestDto;
import ar.gym.gym.dto.response.ClientResponseDto;

import java.util.List;

public interface ClientService{
    ClientResponseDto create(ClientRequestDto clientRequestDto);

    List<ClientResponseDto> findAll();

    ClientResponseDto findById(String id);

    ClientResponseDto update(ClientRequestDto clientRequestDto);

    void delete(String id);
}
