package ar.gym.gym.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionToTrainingDiaryRequestDto {
    private int reps;
    private String exerciseName;
    private Double weight;

}
