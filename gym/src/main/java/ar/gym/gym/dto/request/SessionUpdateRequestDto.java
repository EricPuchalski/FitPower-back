package ar.gym.gym.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionUpdateRequestDto {

    @Min(value = 1, message = "El número mínimo de sets debe ser 1")
    @Max(value = 5, message = "El número máximo de sets debe ser 5")
    private int sets;

    @Min(value = 0, message = "El número mínimo de repeticiones debe ser 0")
    @Max(value = 150, message = "El número máximo de repeticiones debe ser 150")
    private int reps;

    private LocalTime restTime;

    private String exerciseName;

}
