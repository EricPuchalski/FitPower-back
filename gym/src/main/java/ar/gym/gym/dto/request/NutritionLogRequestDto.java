package ar.gym.gym.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NutritionLogRequestDto {
    private Long nutritionPlanId;     // ID del plan de nutrición asociado
    private Long clientId;            // ID del cliente que consume los alimentos
    private LocalDateTime date;       // Fecha y hora del registro
    private float totalCaloriesConsumed; // Total de calorías consumidas en ese día
    private String observations;      // Observaciones o comentarios adicionales
    private boolean completed;        // Indica si el cliente completó el registro
}
