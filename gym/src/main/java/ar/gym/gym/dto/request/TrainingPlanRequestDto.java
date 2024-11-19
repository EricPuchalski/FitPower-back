package ar.gym.gym.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingPlanRequestDto {
    @NotBlank(message = "El dni del cliente es obligatorio")
    private String clientDni;  // Este plan está asociado a un cliente
    @NotBlank(message = "El nombre del plan de entrenamiento es obligatorio")
    private String name;  // Nombre del plan de entrenamiento
    @NotBlank(message = "La descripción del plan de entrenamiento es obligatoria")
    private String description;  // Descripción general del plan
}
