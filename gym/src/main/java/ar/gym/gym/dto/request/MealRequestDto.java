package ar.gym.gym.dto.request;

import ar.gym.gym.model.MealTime;
import lombok.Data;

import java.util.List;

@Data
public class MealRequestDto {
    private MealTime mealTime; // Tiempo de la comida, como desayuno, almuerzo, etc.
    private List<Long> foodIds; // Lista de IDs de los alimentos en esta comida
    private Long nutritionPlanId; // ID del plan de nutrición al que pertenece esta comida
    private boolean completed; // Indica si el cliente consumió esta comida
}
