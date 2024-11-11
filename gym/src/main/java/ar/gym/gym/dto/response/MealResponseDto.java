package ar.gym.gym.dto.response;

import ar.gym.gym.model.MealTime;
import lombok.Data;

import java.util.List;

@Data
public class MealResponseDto {
    private Long id;
    private MealTime mealTime;
    private Long nutritionLogId;
    private boolean completed;
    private List<MealItemResponseDto> mealItems;
}