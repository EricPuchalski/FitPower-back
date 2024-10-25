package ar.gym.gym.controller;

import ar.gym.gym.dto.request.ClientRequestDto;
import ar.gym.gym.dto.response.ClientResponseDto;
import ar.gym.gym.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController{

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientResponseDto> create(@Validated @RequestBody ClientRequestDto clientRequestDto) {
        ClientResponseDto createdClient = clientService.create(clientRequestDto);
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<ClientResponseDto>> findAll() {
        List<ClientResponseDto> clients = clientService.findAll();
        return ResponseEntity.ok(clients);
    }


    @GetMapping("/{dni}")
    public ResponseEntity<ClientResponseDto> findByDni(@PathVariable String dni) {
        ClientResponseDto client = clientService.findByDni(dni);
        return ResponseEntity.ok(client);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDto> update(@Valid @RequestBody ClientRequestDto clientRequestDto, @PathVariable Long id) {
        ClientResponseDto updatedClient = clientService.update(clientRequestDto, id);
        return ResponseEntity.ok(updatedClient);    }

    @PutMapping("/disable/{dni}")
    public ResponseEntity<Void> disable(@PathVariable String dni) {
       clientService.disableClientByDni(dni);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> delete(@PathVariable String dni) {
        clientService.delete(dni);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);    }
}
