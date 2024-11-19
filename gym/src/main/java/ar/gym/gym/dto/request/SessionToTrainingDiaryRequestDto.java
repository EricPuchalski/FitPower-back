package ar.gym.gym.dto.request;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionToTrainingDiaryRequestDto {

    @Min(value = 0, message = "El número mínimo de repeticiones debe ser 0")
    @Max(value = 150, message = "El número máximo de repeticiones debe ser 150")
    private int reps;

    @NotBlank(message = "El nombre del ejercicio es obligatorio")
    private String exerciseName;

    @NotNull(message = "El peso es obligatorio")
    @DecimalMax(value = "1000.0", message = "El peso máximo permitido es 1000 kg")
    @DecimalMin(value = "0.0", inclusive = true, message = "El peso no puede ser negativo")
    private Double weight;

}
