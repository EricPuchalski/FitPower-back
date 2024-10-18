package ar.gym.gym.controller;

import com.itec.FitFlowApp.dto.request.ClientRequestDto;
import com.itec.FitFlowApp.dto.response.ClientResponseDto;
import com.itec.FitFlowApp.model.service.ClientService;
import com.itec.FitFlowApp.util.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fit_flow/clients")
public class ClientController implements Controller<ClientResponseDto, ClientRequestDto> {
    @Autowired
    private ClientService clientService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ClientResponseDto> create(@RequestBody ClientRequestDto clientRequestDto) {
        ClientResponseDto createdClient = clientService.create(clientRequestDto);
        return new ResponseEntity<>(createdClient, HttpStatus.CREATED);    }

    @Override
    @GetMapping
    public ResponseEntity<List<ClientResponseDto>> findAll() {
        List<ClientResponseDto> clients = clientService.findAll();
        return ResponseEntity.ok(clients);
    }

    @Override
    @GetMapping("/{dni}")
    public ResponseEntity<ClientResponseDto> findById(@PathVariable String id) {
        ClientResponseDto client = clientService.findById(id);
        return ResponseEntity.ok(client);
    }

    @Override
    @PutMapping("/{dni}")
    public ResponseEntity<ClientResponseDto> update(@RequestBody ClientRequestDto clientRequestDto) {
        ClientResponseDto updatedClient = clientService.update(clientRequestDto);
        return ResponseEntity.ok(updatedClient);    }

    @Override
    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        clientService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);    }
}
