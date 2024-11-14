package ar.gym.gym.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionToTrainingDiaryResponseDto
{
    private Long id;
    private int reps;
    private String exerciseName;
    private Double weight;
}
