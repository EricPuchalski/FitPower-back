package ar.gym.gym.dto.response;


import lombok.Data;

import java.util.List;
@Data
public class TrainingPlanResponseDto {
    private Long id;
    private String name;
    private String clientDni;
    private List<RoutineResponseDto> routines;
    private boolean active;
}
