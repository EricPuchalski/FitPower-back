package ar.gym.gym.dto.response;

import ar.gym.gym.model.ClientStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientCreateResponseDto {
    private Long id;
    private String name;
    private String lastname;
    private String dni;
    private String initialPhysicalState; //Estado fisico inicial
    private String phone;
    private String address;
    private String email;
    private boolean active;
    private String goal;
    private String gymName;
}
