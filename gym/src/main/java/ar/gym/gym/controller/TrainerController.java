package ar.gym.gym.controller;

import com.itec.FitFlowApp.dto.request.RoutineRequestDto;
import com.itec.FitFlowApp.dto.request.TrainerRequestDto;
import com.itec.FitFlowApp.dto.response.ClientResponseDto;
import com.itec.FitFlowApp.dto.response.RoutineResponseDto;
import com.itec.FitFlowApp.dto.response.TrainerResponseDto;
import com.itec.FitFlowApp.model.service.TrainerService;
import com.itec.FitFlowApp.util.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fit_flow/trainers")
public class TrainerController implements Controller<TrainerResponseDto, TrainerRequestDto> {
    @Autowired
    private TrainerService trainerService;

    @Override
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TrainerResponseDto> create(@RequestBody TrainerRequestDto trainerRequestDto) {
        TrainerResponseDto createdTrainer = trainerService.create(trainerRequestDto);
        return new ResponseEntity<>(createdTrainer, HttpStatus.CREATED);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<TrainerResponseDto>> findAll() {
        List<TrainerResponseDto> trainers = trainerService.findAll();
        return ResponseEntity.ok(trainers);
    }

    @Override
    @GetMapping("/{dni}")
    public ResponseEntity<TrainerResponseDto> findById(@PathVariable String id) {
        TrainerResponseDto trainer = trainerService.findById(id);
        return ResponseEntity.ok(trainer);
    }

    @Override
    @PutMapping("/{dni}")
    public ResponseEntity<TrainerResponseDto> update(@RequestBody TrainerRequestDto trainerRequestDto) {
        TrainerResponseDto updatedTrainer = trainerService.update(trainerRequestDto);
        return ResponseEntity.ok(updatedTrainer);
    }

    @Override
    @DeleteMapping("/{dni}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        trainerService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{dni}")
    public ResponseEntity<List<ClientResponseDto>> getClients(@PathVariable String dni){
        List<ClientResponseDto>trainerClients = trainerService.getClients(dni);
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
