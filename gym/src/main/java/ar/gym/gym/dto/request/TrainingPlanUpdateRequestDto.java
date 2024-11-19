package ar.gym.gym.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingPlanUpdateRequestDto {
    @Pattern(regexp = "^[0-9]{7,8}$", message = "El DNI debe tener entre 7 y 8 dígitos")
    private String clientDni;  // Este plan está asociado a un cliente
    private String name;  // Nombre del plan de entrenamiento
    private String description;  // Descripción general del plan
}
