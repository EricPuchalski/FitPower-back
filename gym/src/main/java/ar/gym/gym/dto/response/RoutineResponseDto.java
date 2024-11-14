package ar.gym.gym.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutineResponseDto {
    private Long id;
    private String clientDNI;
    private String name;
    private LocalDate creationDate;
    private boolean active;
    private List<SessionResponseDto> sessions;
    private boolean completed;
}
