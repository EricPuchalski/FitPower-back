package ar.gym.gym.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseRequestDto {
    @NotBlank(message = "El nombre del ejercicio es obligatorio")
    private String name;
    private String equipment;
    @NotBlank(message = "El grupo muscular es obligatorio")
    private String muscleGroup;
}
