package ar.gym.gym.controller;

import ar.gym.gym.dto.request.RoutineRequestDto;
import ar.gym.gym.dto.request.TrainerRequestDto;
import ar.gym.gym.dto.response.ClientResponseDto;
import ar.gym.gym.dto.response.RoutineResponseDto;
import ar.gym.gym.dto.response.TrainerResponseDto;
import ar.gym.gym.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController{
    @Autowired
    private TrainerService trainerService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TrainerResponseDto> create(@RequestBody TrainerRequestDto trainerRequestDto) {
        TrainerResponseDto createdTrainer = trainerService.create(trainerRequestDto);
        return new ResponseEntity<>(createdTrainer, HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<TrainerResponseDto>> findAll() {
        List<TrainerResponseDto> trainers = trainerService.findAll();
        return ResponseEntity.ok(trainers);
    }


    @GetMapping("/{dni}")
    public ResponseEntity<TrainerResponseDto> findById(@PathVariable String id) {
        TrainerResponseDto trainer = trainerService.findById(id);
        return ResponseEntity.ok(trainer);
    }


    @PutMapping("/{dni}")
    public ResponseEntity<TrainerResponseDto> update(@RequestBody TrainerRequestDto trainerRequestDto) {
        TrainerResponseDto updatedTrainer = trainerService.update(trainerRequestDto);
        return ResponseEntity.ok(updatedTrainer);
    }


    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        trainerService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{dni}")
    public ResponseEntity<List<ClientResponseDto>> getClients(@PathVariable String dni){
        List<ClientResponseDto>trainerClients = trainerService.getClientsAssociated(dni);
        return ResponseEntity.ok(trainerClients);
    }

    @PostMapping("/routines")
    public ResponseEntity<RoutineResponseDto> createRoutineForClient(@RequestBody RoutineRequestDto routineRequestDto) {
        RoutineResponseDto routineResponse = trainerService.createRoutine(routineRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(routineResponse);
    }

    //falta crear la funcion de plan nut
    //creamos rutina y agregamos a trainer
}
