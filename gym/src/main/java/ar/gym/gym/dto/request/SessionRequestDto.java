package ar.gym.gym.dto.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionRequestDto {
    private int sets;
    private int reps;
    private LocalTime restTime;
    private boolean completed;
    private String exerciseName;
    private Double weight;

}
