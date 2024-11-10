package ar.gym.gym.dto.response;

import ar.gym.gym.model.FoodCategory;
import lombok.Data;

@Data
public class FoodResponseDto {
    private Long id;             // ID único del alimento
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
    private Double quantity;     // Cantidad
    private String measureUnit;  // Unidad de medida
    private FoodCategory category; // Categoría del alimento
    private boolean completed;   // Indica si se consumió
}
