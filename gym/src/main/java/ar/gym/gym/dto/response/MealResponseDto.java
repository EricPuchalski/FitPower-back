package ar.gym.gym.dto.response;

import ar.gym.gym.model.MealDetail;
import lombok.Data;

import java.util.List;

@Data
public class MealResponseDto {
        private Long id;
        private String mealTime;
        private Long nutritionLogId;
        private boolean completed;
        private List<MealDetail> mealDetails;
}