package ar.gym.gym.dto.response;

import lombok.Data;

@Data
public class MealDetailResponseDto {
    private Long id;
    private String foodName;
    private Integer quantity;
    private String measureUnit;
}
