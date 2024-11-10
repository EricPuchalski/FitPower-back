package ar.gym.gym.dto.response;

import ar.gym.gym.model.MealTime;
import lombok.Data;

import java.util.List;

@Data
public class MealResponseDto {
    private Long id; // ID único de la comida
    private MealTime mealTime; // Tiempo de la comida, como desayuno, almuerzo, etc.
    private List<String> foodNames; // Lista de nombres de los alimentos en esta comida
    private Long nutritionPlanId; // ID del plan de nutrición al que pertenece esta comida
    private boolean completed; // Indica si el cliente consumió esta comida
}
