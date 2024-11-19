package ar.gym.gym.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NutritionistRequestDto {
    @NotBlank(message = "El nombre del nutricionista es obligatorio")
    private String name;
    @NotBlank(message = "El apellido del nutricionista es obligatorio")
    private String lastname;
    @Pattern(regexp = "^[0-9]{7,8}$", message = "El DNI debe tener entre 7 y 8 dígitos")
    @NotBlank(message = "El dni del nutricionista es obligatorio")
    private String dni;
    @NotBlank(message = "El teléfono es obligatorio.")
    private String phone;
    private String address;
    @Email(message = "El correo electrónico debe ser válido")
    @NotBlank(message = "El email del nutricionista es obligatorio")
    private String email;
    @NotBlank(message = "La profesión del nutricionista es obligatoria")
    private String profession;
    @NotBlank(message = "El gimnasio asignado es obligatorio")
    private String gymName;
}
