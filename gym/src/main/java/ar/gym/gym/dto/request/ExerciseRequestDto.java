package ar.gym.gym.dto.request;

import com.itec.FitFlowApp.model.entity.Session;
import lombok.Data;

import java.time.LocalTime;
@Data
public class ExerciseRequestDto {
    private Long id;
    private String name;
    private String equipment;
    private float weight;
    private LocalTime restTime;
    private LocalTime duration;
    private Session session;
}
