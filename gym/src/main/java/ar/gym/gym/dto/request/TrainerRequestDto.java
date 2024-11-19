package ar.gym.gym.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerRequestDto {
    @NotBlank(message = "El nombre del entrenador es obligatorio")
    private String name;
    @NotBlank(message = "El apellido del entrenador es obligatorio")
    private String lastname;
    @NotBlank(message = "El dni del entrenador es obligatorio")
    @Pattern(regexp = "^[0-9]{7,8}$", message = "El DNI debe tener entre 7 y 8 dígitos")
    private String dni;
    @NotBlank(message = "El teléfono es obligatorio.")
    private String phone;
    @NotBlank(message = "La dirección del entrenador es obligatoria")
    private String address;
    @Email(message = "El correo electrónico debe ser válido")
    private String email;
    @NotBlank(message = "La profesión del entrenador es obligatoria")
    private String profession;
    @NotBlank(message = "El gimnasio del entrenador es obligatorio")
    private String gymName;
}
