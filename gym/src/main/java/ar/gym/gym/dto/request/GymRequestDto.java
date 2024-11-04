package ar.gym.gym.dto.request;

import ar.gym.gym.model.Client;
import ar.gym.gym.model.Nutritionist;
import ar.gym.gym.model.Trainer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GymRequestDto {
    private String name;
    private String phone;
    private String email;
    private String address;

}
