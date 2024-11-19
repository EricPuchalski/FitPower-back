package ar.gym.gym.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoutineRequestDto {
    @NotBlank(message = "El dni del cliente es obligatorio")
    @Pattern(regexp = "^[0-9]{7,8}$", message = "El DNI debe tener entre 7 y 8 dígitos")
    private String clientDni; // Cambiar Client completo por solo el DNI
    @NotBlank(message = "El nombre de la rutina es obligatoria")
    private String name;
}
