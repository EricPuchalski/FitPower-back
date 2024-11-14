package ar.gym.gym.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponseDto {
    private Long id;
    private String message;
    private boolean seen;
    private LocalDateTime creationDate;
    private String clientDni;
}
