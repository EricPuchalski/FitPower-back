package ar.gym.gym.dto.request;

import ar.gym.gym.model.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientRequestDto {
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
    private ClientStatus status;
    private String goal;
    private String gymName;
}
