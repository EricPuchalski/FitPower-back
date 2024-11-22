package ar.gym.gym.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingDiaryRequestDto {
    private SessionRequestDto session;
    @NotBlank(message = "El dni del cliente es obligatorio")
    private String clientDni;
    private String observation;
}
