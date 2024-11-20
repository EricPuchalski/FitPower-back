package ar.gym.gym.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientStatusResponseDto {
    private Long id;
    private Double weight;
    private Double height;
    private LocalDateTime creationDate;

}