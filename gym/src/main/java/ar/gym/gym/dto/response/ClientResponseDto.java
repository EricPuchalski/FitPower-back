package ar.gym.gym.dto.response;

import com.itec.FitFlowApp.model.entity.*;
import lombok.Data;

import java.util.List;

@Data
public class ClientResponseDto {
    private String name;
    private String surname;
    private String dni;
    private String phone;
    private String address;
    private String email;
    private boolean active;
    private ClientStatus initState;
    private ClientStatus currentState;
    private String goal;
    private Gym gym;
    private Trainer trainer;
    private Nutritionist nutritionist;
    private List<Routine>routines;
    private List<NutritionalPlan>nutritionalPlans;
    private Record record;
    private List<TrainingDiary>trainingDiaryList;
    private List<NutritionalDiary>nutritionalDiaryList;
}
