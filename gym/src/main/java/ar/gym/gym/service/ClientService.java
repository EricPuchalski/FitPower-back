package ar.gym.gym.service;

import ar.gym.gym.dto.request.ClientRequestDto;
import ar.gym.gym.dto.request.ClientStatusRequestDto;
import ar.gym.gym.dto.request.ClientUpdateRequestDto;
import ar.gym.gym.dto.response.*;
import ar.gym.gym.model.Performance;

import java.util.List;

public interface ClientService{
    ClientCreateResponseDto create(ClientRequestDto clientRequestDto);

    List<ClientResponseDto> findAll();

    ClientResponseDto findByDni(String dni);

    ClientUpdateResponseDto update(ClientUpdateRequestDto clientRequestDto, Long id);

    void delete(String id);


    ClientResponseDto disableClientByDni(String dni);
    ClientResponseDto findByEmail(String email);
    List<ClientStatusResponseDto> findClientStatusesByDni(String dni);
    // Método para agregar un nuevo estado a un cliente por DNI
    ClientStatusResponseDto addClientStatus(String dni, ClientStatusRequestDto newStatusRequestDto);
    List<NotificationResponseDto> findByDniAndNotificationsSeenFalse(String dni);
    NotificationResponseDto markNotificationAsSeen(String dni, Long notificationId);

    List<ClientResponseDto> findAllByActiveTrue();
    PerfomanceResponseDto getPerformanceByClientDni(String dni);
}
