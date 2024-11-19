package ar.gym.gym.dto.request;

import ar.gym.gym.model.ClientStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientRequestDto {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    @NotBlank(message = "El apellido es obligatorio")
    private String lastname;
    @Pattern(regexp = "^[0-9]{7,8}$", message = "El DNI debe tener entre 7 y 8 dígitos")
    @NotBlank(message = "El dni es obligatorio")
    private String dni;
    private String phone;
    private String address;
    @Email(message = "El correo electrónico debe ser válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;
    @NotBlank(message = "Los objetivos son obligatorios")
    private String goal;
    @NotBlank(message = "El estado fisico inicial es obligatorio")
    private String initialPhysicalState; //Estado fisico inicial
    @NotBlank(message = "El gimnasio es obligatorio")
    private String gymName;
}
