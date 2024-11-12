package ar.gym.gym.dto.request;

import ar.gym.gym.model.MealTime;
import lombok.Data;

@Data
public class MealRequestDto {
    private MealTime mealTime;
    private Boolean completed;
}
