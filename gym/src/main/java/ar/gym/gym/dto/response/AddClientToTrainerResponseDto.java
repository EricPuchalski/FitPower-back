package ar.gym.gym.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddClientToTrainerResponseDto {
    private String trainerDni;
    private String clientDni;
    private LocalDateTime registrationDate;
}
