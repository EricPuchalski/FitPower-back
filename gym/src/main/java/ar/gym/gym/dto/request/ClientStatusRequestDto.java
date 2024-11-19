package ar.gym.gym.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientStatusRequestDto {
    @NotBlank(message = "El peso es obligatorio")
    private Double weight;
    @NotBlank(message = "La altura es obligatorio")
    private Double height;
}
