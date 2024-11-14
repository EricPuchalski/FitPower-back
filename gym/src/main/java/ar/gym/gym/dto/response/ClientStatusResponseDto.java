package ar.gym.gym.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientStatusResponseDto {
    private Long id;
    private Double weight;
    private Double height;
    private Double bodymass;
    private Double bodyfat;
    private LocalDate creationDate;

}