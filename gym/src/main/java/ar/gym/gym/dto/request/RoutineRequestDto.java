package ar.gym.gym.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutineRequestDto {
    private Long trainingPlanId; // Cambiar Client completo por solo el DNI
    private String name;

}
