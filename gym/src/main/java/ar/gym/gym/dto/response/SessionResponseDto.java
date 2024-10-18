package ar.gym.gym.dto.response;

import com.itec.FitFlowApp.model.entity.Exercise;
import com.itec.FitFlowApp.model.entity.Routine;
import com.itec.FitFlowApp.model.entity.TrainingDiary;
import lombok.Data;

import java.time.LocalTime;

@Data
public class SessionResponseDto {
    private Long id;
    private String trainingDay;
    private String muscleGroup;
    private int sets;
    private int reps;
    private LocalTime restTime;
    private LocalTime duration;
    private boolean completed;
    private Exercise exercise;
    private Routine routine;
    private TrainingDiary trainingDiary;
}
