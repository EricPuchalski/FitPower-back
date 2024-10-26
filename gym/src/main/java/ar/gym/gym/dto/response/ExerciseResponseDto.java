package ar.gym.gym.dto.response;

import ar.gym.gym.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseResponseDto {
    private String name;
    private String equipment;
    private String muscleGroup;
}
