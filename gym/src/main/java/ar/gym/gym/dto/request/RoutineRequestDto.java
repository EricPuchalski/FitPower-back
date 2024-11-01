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
    private String name;
//    private Status status;

}
