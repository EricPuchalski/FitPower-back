package ar.gym.gym.dto.request;
import lombok.Data;
@Data
public class MealItemRequestDto {
    private Long foodId;      // ID del alimento
    private String foodName;  // Nombre del alimento
    private Integer quantity; // Cantidad
    private String measureUnit; // Unidad de medida
}
