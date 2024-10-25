package ar.gym.gym.dto.response;

import ar.gym.gym.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GymResponseDto {
    private Long id;
    private String gymCode;
    private String name;
    private String phone;
    private String email;
    private String address;
    private List<ClientResponseDto> clientList;
    private List<TrainerResponseDto> trainerList;
    private List<NutritionistResponseDto> nutritionistList;
}
