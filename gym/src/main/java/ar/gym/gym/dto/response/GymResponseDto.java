package ar.gym.gym.dto.response;

import com.itec.FitFlowApp.model.entity.Client;
import com.itec.FitFlowApp.model.entity.Nutritionist;
import com.itec.FitFlowApp.model.entity.Trainer;
import lombok.Data;

import java.util.List;

@Data
public class GymResponseDto {
    private String gymCode;
    private String name;
    private String phone;
    private String email;
    private String address;
    private List<Client> clientList;
    private List<Trainer>trainerList;
    private List<Nutritionist>nutritionistList;
}
