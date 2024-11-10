package ar.gym.gym.dto.request;

import ar.gym.gym.model.NutritionStatus;
import lombok.Data;

import java.util.Date;

@Data
public class NutritionPlanRequestDto {

    private Long clientId;           // ID del cliente que recibe el plan
    private Long nutritionistId;     // ID del nutricionista que asigna el plan
    private String type;             // Tipo de plan (por ejemplo, bajo en carbohidratos, balanceado, etc.)
    private Date startDate;          // Fecha de inicio del plan
    private Date endDate;            // Fecha de fin del plan
    private String description;      // Descripción del plan
    private Double dailyCalories;    // Calorías diarias recomendadas
    private NutritionStatus status;  // Estado del plan (por ejemplo, ACTIVO, COMPLETADO, EN_PROGRESO)
    private boolean active;          // Indica si el plan está activo
    private boolean completed;       // Indica si el cliente ha finalizado el plan
}
