package ar.gym.gym.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingPlanRequestDto {
    private String clientDni;  // Este plan está asociado a un cliente
    private String name;  // Nombre del plan de entrenamiento
    private String description;  // Descripción general del plan
}
