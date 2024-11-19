package ar.gym.gym.dto.request;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionRequestDto {

    @Min(value = 1, message = "El número mínimo de sets debe ser 1")
    @Max(value = 5, message = "El número máximo de sets debe ser 5")
    private int sets;

    @Min(value = 0, message = "El número mínimo de repeticiones debe ser 0")
    @Max(value = 150, message = "El número máximo de repeticiones debe ser 150")
    private int reps;

    private LocalTime restTime;

    @NotBlank(message = "El nombre del ejercicio es obligatorio")
    private String exerciseName;

}
