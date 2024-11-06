package ar.gym.gym.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class TrainingDiaryResponseDto {
    private List<SessionResponseDto> sessions;
    private String clientDni;
    private LocalDateTime date;
    private String observation;
}
