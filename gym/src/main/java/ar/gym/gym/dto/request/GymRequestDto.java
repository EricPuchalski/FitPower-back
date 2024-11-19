package ar.gym.gym.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GymRequestDto {
    @NotBlank(message = "El nombre del gimnasio es obligatorio")
    private String name;
    @NotBlank(message = "El teléfono del gimnasio es obligatorio")
    private String phone;
    @NotBlank(message = "El email del gimnasio es obligatorio")
    private String email;
    @NotBlank(message = "La ubicación del gimnasio es obligatoria")
    private String address;
}
