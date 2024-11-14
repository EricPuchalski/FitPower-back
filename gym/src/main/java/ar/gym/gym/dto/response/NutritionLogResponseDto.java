package ar.gym.gym.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NutritionLogResponseDto {
    private Long id;                          // ID del registro de nutrición
    private Long nutritionPlanId;             // ID del plan de nutrición asociado
    private String clientDni;                    // ID del cliente que hizo el registro
    private LocalDateTime date;               // Fecha y hora del registro
    private Float dailyCalories;       //calorias que debe cumplir diariamente
    private Float totalCaloriesConsumed;      // Total de calorías consumidas en ese día
    private String observations;              // Observaciones sobre el consumo de alimentos
    private boolean completed;                // Indica si el cliente completó el registro
    private List<MealResponseDto> mealList;//cambio a MealResponseDto, para enviar al cliente lo necesario,
}
