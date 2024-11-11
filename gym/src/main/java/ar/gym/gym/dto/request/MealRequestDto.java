package ar.gym.gym.dto.request;

import ar.gym.gym.model.MealItem;
import ar.gym.gym.model.MealTime;
import lombok.Data;

import java.util.List;

@Data
public class MealRequestDto {
    private Long nutritionLogId;
    private MealTime mealTime;
    private Boolean completed;
    private List<MealItemRequestDto> mealItems;
}
