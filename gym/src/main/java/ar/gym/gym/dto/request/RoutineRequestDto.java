package ar.gym.gym.dto.request;

import ar.gym.gym.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutineRequestDto {
    private String clientDni; // Cambiar Client completo por solo el DNI
    private String trainerDni; // Cambiar Trainer completo por solo el DNI
    private String routineCode;
    private String routineType;
    private LocalDate creationDate;
    private  LocalDate startDate;
    private boolean active;
    private List<Session> sessions;
//    private Status status;

}
