package ar.gym.gym.dto.response;

import lombok.Data;

@Data
public class MealItemResponseDto {
    private Long id;
    private String foodName;
    private Integer quantity;
    private String measureUnit;
}
