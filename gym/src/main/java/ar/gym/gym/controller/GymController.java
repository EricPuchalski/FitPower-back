package ar.gym.gym.controller;

import ar.gym.gym.dto.request.GymRequestDto;
import ar.gym.gym.dto.response.AddClientToNutritionistResponseDto;
import ar.gym.gym.dto.response.AddClientToTrainerResponseDto;
import ar.gym.gym.dto.response.GymResponseDto;
import ar.gym.gym.model.Gym;
import ar.gym.gym.service.GymService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/gyms")
public class GymController{

    private final GymService gymService;

    public GymController(GymService gymService) {
        this.gymService = gymService;
    }

    // Endpoint para crear un nuevo gimnasio
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<GymResponseDto> create(@RequestBody GymRequestDto gymRequestDto) {
        GymResponseDto createdGym = gymService.create(gymRequestDto);
        return new ResponseEntity<>(createdGym, HttpStatus.CREATED);
    }

    // Endpoint para obtener todos los gimnasios
    @GetMapping
    public ResponseEntity<List<GymResponseDto>> findAll() {
        List<GymResponseDto> gyms = gymService.findAll();
        return ResponseEntity.ok(gyms);
    }

    // Endpoint para obtener un gimnasio por código
    @GetMapping("/{name}")
    public ResponseEntity<Gym> findByName(@PathVariable String name) {
        Optional<Gym> gym = gymService.findByName(name);
        return ResponseEntity.ok(gym.get());
    }

    // Endpoint para actualizar un gimnasio existente
    @PutMapping("/{id}")
    public ResponseEntity<GymResponseDto> update(@RequestBody GymRequestDto gymRequestDto, @PathVariable Long id) {
        GymResponseDto updatedGym = gymService.update(gymRequestDto, id);
        return ResponseEntity.ok(updatedGym);
    }

    // Endpoint para eliminar un gimnasio
    @DeleteMapping("/{gymCode}")
    public ResponseEntity<Void> delete(@PathVariable String gymCode) {
        gymService.deleteByGymCode(gymCode);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Endpoint para agregar un cliente a un gimnasio
    @PutMapping("/add/{gymCode}/clients/{dni}")
    public ResponseEntity<GymResponseDto> addClientToGym(@PathVariable String gymCode, @PathVariable String dni) {
        GymResponseDto updatedGym = gymService.addClientToGym(gymCode, dni);
        return ResponseEntity.ok(updatedGym);
    }

    // Endpoint para agregar un entrenador a un gimnasio
    @PostMapping("/add/{gymCode}/trainers/{dni}")
    public ResponseEntity<GymResponseDto> addTrainerToGym(@PathVariable String gymCode, @PathVariable String dni) {
        GymResponseDto updatedGym = gymService.addTrainerToGym(gymCode, dni);
        return ResponseEntity.ok(updatedGym);
    }

    // Endpoint para agregar un nutricionista a un gimnasio
    @PostMapping("/add/{gymCode}/nutritionist/{dni}")
    public ResponseEntity<GymResponseDto> addNutritionistToGym(@PathVariable String gymCode, @PathVariable String dni) {
        GymResponseDto updatedGym = gymService.addNutritionistToGym(gymCode, dni);
        return ResponseEntity.ok(updatedGym);
    }

    //Endpoint para asignar un entrenador a un cliente
    @PostMapping("/assign/{dniTrainer}/trainer-to-client/{dniClient}")
    public ResponseEntity<AddClientToTrainerResponseDto>assignTrainerToClient(@PathVariable String dniTrainer, @PathVariable String dniClient){
        AddClientToTrainerResponseDto addClientToTrainerResponseDto = gymService.assignTrainerToClient(dniTrainer,dniClient);
        return ResponseEntity.ok(addClientToTrainerResponseDto);
    }

    //Endpoint para asignar un entrenador a un cliente
    @PutMapping("/assign/{dniNut}/nutritionist-to-client/{dniClient}")
    public ResponseEntity<AddClientToNutritionistResponseDto>assignNutritionistToClient(@PathVariable String dniNut, @PathVariable String dniClient){
        AddClientToNutritionistResponseDto addClientToNutritionistResponseDto = gymService.assignNutritionistToClient(dniNut,dniClient);
        return ResponseEntity.ok(addClientToNutritionistResponseDto);
    }

}
