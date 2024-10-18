package ar.gym.gym.dto.response;

import com.itec.FitFlowApp.model.entity.Session;
import lombok.Data;

import java.time.LocalTime;
@Data
public class ExerciseResponseDto {
    private String name;
    private String equipment;
    private float weight;
    private LocalTime restTime;
    private LocalTime duration;
    private Session session;
}
