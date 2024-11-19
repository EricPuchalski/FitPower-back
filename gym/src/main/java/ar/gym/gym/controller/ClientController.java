package ar.gym.gym.controller;

import ar.gym.gym.dto.request.ClientRequestDto;
import ar.gym.gym.dto.request.ClientStatusRequestDto;
import ar.gym.gym.dto.request.ClientUpdateRequestDto;
import ar.gym.gym.dto.response.*;
import ar.gym.gym.service.ClientService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;
    private static final Logger logger = LoggerFactory.getLogger(ClientController.class);

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ClientCreateResponseDto> create(@Validated @RequestBody ClientRequestDto clientRequestDto) {
        logger.info("Creating a new client: {}", clientRequestDto);
        ClientCreateResponseDto createdClient = clientService.create(clientRequestDto);
        logger.info("Client created successfully: {}", createdClient);
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ClientResponseDto>> findAll() {
        logger.info("Fetching all clients");
        List<ClientResponseDto> clients = clientService.findAll();
        logger.info("Retrieved {} clients", clients.size());
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ClientResponseDto>> findAllByActiveTrue() {
        logger.info("Fetching all active clients");
        List<ClientResponseDto> activeClients = clientService.findAllByActiveTrue();
        logger.info("Retrieved {} active clients", activeClients.size());
        return ResponseEntity.ok(activeClients);
    }

    @GetMapping("/{dni}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ClientResponseDto> findByDni(@PathVariable String dni) {
        logger.info("Fetching client with DNI: {}", dni);
        ClientResponseDto client = clientService.findByDni(dni);
        logger.info("Client retrieved: {}", client);
        return ResponseEntity.ok(client);
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<ClientResponseDto> findByEmail(@PathVariable String email) {
        logger.info("Fetching client with email: {}", email);
        ClientResponseDto client = clientService.findByEmail(email);
        logger.info("Client retrieved: {}", client);
        return ResponseEntity.ok(client);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ClientUpdateResponseDto> update(@Validated @RequestBody ClientUpdateRequestDto clientRequestDto, @PathVariable Long id) {
        logger.info("Updating client with ID: {}", id);
        ClientUpdateResponseDto updatedClient = clientService.update(clientRequestDto, id);
        logger.info("Client updated successfully: {}", updatedClient);
        return ResponseEntity.ok(updatedClient);
    }

    @PutMapping("/disable/{dni}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> disable(@PathVariable String dni) {
        logger.info("Disabling client with DNI: {}", dni);
        clientService.disableClientByDni(dni);
        logger.info("Client with DNI {} has been disabled", dni);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{dni}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String dni) {
        logger.info("Deleting client with DNI: {}", dni);
        clientService.delete(dni);
        logger.info("Client with DNI {} has been deleted", dni);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Endpoint para obtener los estados de un cliente por DNI
    @GetMapping("/{dni}/statuses")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_TRAINER')")
    public ResponseEntity<List<ClientStatusResponseDto>> getClientStatusesByDni(@PathVariable String dni) {
        List<ClientStatusResponseDto> statuses = clientService.findClientStatusesByDni(dni);
        return ResponseEntity.ok(statuses);
    }

    // Endpoint para agregar un estado a un cliente por DNI
    @PostMapping("/{dni}/statuses")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_TRAINER')")
    public ResponseEntity<ClientStatusResponseDto> addClientStatus(
            @PathVariable String dni,
            @RequestBody ClientStatusRequestDto newStatusRequestDto) {

        ClientStatusResponseDto statusResponseDto = clientService.addClientStatus(dni, newStatusRequestDto);
        return ResponseEntity.ok(statusResponseDto);
    }

    @GetMapping("/{dniClient}/notifications")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<List<NotificationResponseDto>> getUnseenNotificationsByDni(@PathVariable String dniClient) {
        // Llamamos al servicio para obtener las notificaciones no vistas
        List<NotificationResponseDto> unseenNotifications = clientService.findByDniAndNotificationsSeenFalse(dniClient);

        // Devuelve las notificaciones no vistas, incluso si la lista está vacía (responderá con 200 OK y lista vacía)
        return ResponseEntity.ok(unseenNotifications);
    }

    @PutMapping("/{dniClient}/notifications/{notificationId}/mark")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<NotificationResponseDto> markNotificationAsSeen(
            @PathVariable String dniClient,
            @PathVariable Long notificationId) {

        // Llamamos al servicio para marcar la notificación como vista
        NotificationResponseDto updatedNotification = clientService.markNotificationAsSeen(dniClient, notificationId);

        // Devuelve la notificación actualizada con un código 200 OK
        return ResponseEntity.ok(updatedNotification);
    }
}
