package ar.gym.gym.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutineRequestDto {
    @NotBlank(message = "El dni del cliente es obligatorio")
    private String clientDni; // Cambiar Client completo por solo el DNI
    @NotBlank(message = "El nombre de la rutina es obligatoria")
    private String name;
}
