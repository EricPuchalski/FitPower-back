package ar.gym.gym.dto.response;

import ar.gym.gym.model.MealTime;
import lombok.Data;

import java.util.List;

@Data
public class MealResponseDto {
    private Long id;
    private MealTime mealTime; // Tiempo de la comida, como desayuno, almuerzo, etc.
    private int quantity;      // Cantidad de comidas.
    private String measureUnit;   // Unidad de medida, como "grams", "ml", o "piece"
    private List<Long> foodIds; // Lista de IDs de los alimentos en esta comida
    private boolean completed; // Indica si el cliente consumió esta comida
}
