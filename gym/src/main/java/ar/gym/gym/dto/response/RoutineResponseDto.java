package ar.gym.gym.dto.response;

import com.itec.FitFlowApp.model.entity.Client;
import com.itec.FitFlowApp.model.entity.Session;
import com.itec.FitFlowApp.model.entity.Trainer;
import com.itec.FitFlowApp.util.Status;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
@Data
public class RoutineResponseDto {
    private Client client;
    private Trainer trainer;
    private String routineCode;
    private String routineType;
    private LocalDate creationDate;
    private  LocalDate startDate;
    private boolean active;
    private List<Session> sessions;
    private Status status;
}
