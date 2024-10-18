package ar.gym.gym.controller;

import com.itec.FitFlowApp.dto.request.GymRequestDto;
import com.itec.FitFlowApp.dto.response.GymResponseDto;
import com.itec.FitFlowApp.model.service.GymService;
import com.itec.FitFlowApp.util.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fit_flow/gyms")
public class GymController implements Controller<GymResponseDto, GymRequestDto> {
    @Autowired
    private GymService gymService;

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
    @GetMapping("/{gymCode}")
    public ResponseEntity<GymResponseDto> findById(@PathVariable String gymCode) {
        GymResponseDto gym = gymService.findById(gymCode);
        return ResponseEntity.ok(gym);
    }

    // Endpoint para actualizar un gimnasio existente
    @PutMapping("/{gymCode}")
    public ResponseEntity<GymResponseDto> update(@RequestBody GymRequestDto gymRequestDto) {
        GymResponseDto updatedGym = gymService.update(gymRequestDto);
        return ResponseEntity.ok(updatedGym);
    }

    // Endpoint para eliminar un gimnasio
    @DeleteMapping("/{gymCode}")
    public ResponseEntity<Void> delete(@PathVariable String gymCode) {
        gymService.delete(gymCode);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Endpoint para agregar un cliente a un gimnasio
    @PostMapping("/add/{gymCode}/clients/{dni}")
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
    public ResponseEntity<String>assignTrainerToClient(@PathVariable String dniTrainer, @PathVariable String dniClient){
        gymService.assignTrainerToClient(dniTrainer,dniClient);
        return ResponseEntity.ok("Entrenador asignado correctamente al cliente.");
    }

    //Endpoint para asignar un entrenador a un cliente
    @PostMapping("/assign/{dniNut}/nutritionist-to-client/{dniClient}")
    public ResponseEntity<String>assignNutritionistToClient(@PathVariable String dniNut, @PathVariable String dniClient){
        gymService.assignNutritionistToClient(dniNut,dniClient);
        return ResponseEntity.ok("Nutricionista asignado correctamente al cliente.");
    }

}
