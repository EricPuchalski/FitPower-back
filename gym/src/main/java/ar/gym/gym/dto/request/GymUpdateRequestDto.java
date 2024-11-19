package ar.gym.gym.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GymUpdateRequestDto {
    private String name;
    private String phone;
    @Email(message = "El email del gimnasio debe ser valido")
    private String email;
    private String address;
}
