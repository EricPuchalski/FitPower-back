package ar.gym.gym.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingDiaryResponseDto {
    private Long id;
    private List<SessionToTrainingDiaryResponseDto> sessions;
    private String clientDni;
    private LocalDateTime date;
    private String observation;
}
