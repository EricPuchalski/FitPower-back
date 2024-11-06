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
    private String name;
    private String lastname;
    @Pattern(regexp = "^[0-9]{8}$", message = "El DNI debe tener exactamente 8 dígitos")
    private String dni;
    @NotBlank(message = "El teléfono es obligatorio.")
    private String phone;
    private String address;
    @Email(message = "El correo electrónico debe ser válido")
    private String email;
    private boolean active;
    private String profession;
    private String gymName;
}
