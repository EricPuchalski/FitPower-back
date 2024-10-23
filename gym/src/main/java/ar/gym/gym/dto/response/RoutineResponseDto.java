package ar.gym.gym.dto.response;

import ar.gym.gym.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutineResponseDto {
    private Client client;
    private Trainer trainer;
    private String routineCode;
    private String routineType;
    private LocalDate creationDate;
    private  LocalDate startDate;
    private boolean active;
    private List<Session> sessions;
//    private Status status;
}
