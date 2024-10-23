package ar.gym.gym.controller;

import ar.gym.gym.dto.request.ClientRequestDto;
import ar.gym.gym.dto.response.ClientResponseDto;
import ar.gym.gym.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController{

    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<ClientResponseDto> create(@RequestBody ClientRequestDto clientRequestDto) {
        ClientResponseDto createdClient = clientService.create(clientRequestDto);
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);    }


    @GetMapping
    public ResponseEntity<List<ClientResponseDto>> findAll() {
        List<ClientResponseDto> clients = clientService.findAll();
        return ResponseEntity.ok(clients);
    }


    @GetMapping("/{dni}")
    public ResponseEntity<ClientResponseDto> findById(@PathVariable String id) {
        ClientResponseDto client = clientService.findById(id);
        return ResponseEntity.ok(client);
    }


    @PutMapping("/{dni}")
    public ResponseEntity<ClientResponseDto> update(@RequestBody ClientRequestDto clientRequestDto) {
        ClientResponseDto updatedClient = clientService.update(clientRequestDto);
        return ResponseEntity.ok(updatedClient);    }


    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        clientService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);    }
}
