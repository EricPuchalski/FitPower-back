package ar.gym.gym.dto.response;

import ar.gym.gym.model.NutritionStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class NutritionPlanResponseDto {
    private Long id;                  // ID del plan de nutrición
    private Long clientId;            // ID del cliente que tiene el plan
    private Long nutritionistId;      // ID del nutricionista asignado
    private String type;              // Tipo del plan
    private Date startDate;           // Fecha de inicio
    private Date endDate;             // Fecha de fin
    private String description;       // Descripción del plan
    private Double dailyCalories;     // Calorías diarias
    private NutritionStatus status;   // Estado del plan
    private boolean active;           // Indica si el plan está activo
    private boolean completed;        // Indica si el plan fue completado


}