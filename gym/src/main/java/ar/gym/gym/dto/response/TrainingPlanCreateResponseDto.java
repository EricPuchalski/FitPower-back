package ar.gym.gym.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingPlanCreateResponseDto {
    private Long id;
    private String clientDni ;  // Este plan está asociado a un cliente
    private boolean active; //Saber cual es el plan activo del cliente
    private String name;  // Nombre del plan de entrenamiento
    private String description;  // Descripción general del plan
 }
