package ar.gym.gym.dto.request;

import ar.gym.gym.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseRequestDto {
    private Long id;
    private String name;
    private String equipment;
    private float weight;
    private LocalTime restTime;
    private LocalTime duration;
    private Session session;
}
