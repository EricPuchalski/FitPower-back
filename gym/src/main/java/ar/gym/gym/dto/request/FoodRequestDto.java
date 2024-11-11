package ar.gym.gym.dto.request;

import ar.gym.gym.model.FoodCategory;
import lombok.Data;

@Data
public class FoodRequestDto {
    private String name;         // Nombre del alimento
    private Double calories;     // Calorías
    private Double protein;      // Proteínas
    private Double carbs;        // Carbohidratos
    private Double fats;         // Grasas
    private Double fiber;        // Fibra
    private Double sugar;        // Azúcar
    private Double sodium;       // Sodio
    private Double iron;         // Hierro
    private Double potassium;    // Potasio
    private Double calcium;      // Calcio
    private FoodCategory category; // Categoría del alimento
    private Long mealId;         // ID de la comida a la que pertenece este alimento
    private boolean completed;   // Indica si se consumió
}
