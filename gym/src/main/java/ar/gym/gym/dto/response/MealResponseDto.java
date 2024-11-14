package ar.gym.gym.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class MealResponseDto {
        private Long id; //
        private String mealTime;
        private Long nutritionLogId;
        private boolean completed;
        private List<MealDetailResponseDto> mealDetails;
}