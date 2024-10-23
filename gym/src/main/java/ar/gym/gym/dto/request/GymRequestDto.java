package ar.gym.gym.dto.request;

import ar.gym.gym.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GymRequestDto {
    private String gymCode;
    private String name;
    private String phone;
    private String email;
    private String address;
    private List<Client> clientList;
    private List<Trainer>trainerList;
    private List<Nutritionist>nutritionistList;
}
