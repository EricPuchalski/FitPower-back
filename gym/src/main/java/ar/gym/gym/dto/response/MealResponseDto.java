package ar.gym.gym.dto.response;

import ar.gym.gym.model.Food;
import ar.gym.gym.model.MealTime;
import lombok.Data;

import java.util.Map;

@Data
public class MealResponseDto {
    private Long id;
    private MealTime mealTime; // Tiempo de la comida, como desayuno, almuerzo, etc.
    private Map<Food, Integer> foods; // Mapa de alimentos y sus cantidades en esta comida
    private String measureUnit;   // Unidad de medida, como "grams", "ml", o "piece"
    private boolean completed; // Indica si el cliente consumió esta comida
}