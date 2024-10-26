package ar.gym.gym.service;

import ar.gym.gym.dto.request.ClientRequestDto;
import ar.gym.gym.dto.response.ClientResponseDto;
import ar.gym.gym.model.Client;

import java.util.List;

public interface ClientService{
    ClientResponseDto create(ClientRequestDto clientRequestDto);

    List<ClientResponseDto> findAll();

    ClientResponseDto findByDni(String dni);

    ClientResponseDto update(ClientRequestDto clientRequestDto, Long id);

    void delete(String id);


    ClientResponseDto disableClientByDni(String dni);

//    ClientResponseDto findByEmail(String email);



}
