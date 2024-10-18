package ar.gym.gym.dto.request;

import com.itec.FitFlowApp.model.entity.Client;
import com.itec.FitFlowApp.model.entity.Gym;
import com.itec.FitFlowApp.model.entity.NutritionalPlan;
import lombok.Data;

import java.util.List;

@Data
public class NutritionistRequestDto {
    private Long id;
    private String name;
    private String surname;
    private String dni;
    private String phone;
    private String address;
    private String email;
    private boolean active;
    private String profession;
    private boolean available;
    private List<NutritionalPlan> nutritionalPlanList;
    private List<Client>clients;
    private Gym gym;
}
