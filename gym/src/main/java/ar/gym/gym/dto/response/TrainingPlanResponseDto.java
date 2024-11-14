package ar.gym.gym.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingPlanResponseDto {
    private Long id;
    private String clientDni ;  // Este plan está asociado a un cliente
    private boolean active; //Saber cual es el plan activo del cliente
    private String name;  // Nombre del plan de entrenamiento
    private String description;  // Descripción general del plan
    private List<RoutineResponseDto> routines;  // Listado de rutinas asociadas al plan de entrenamiento
}
