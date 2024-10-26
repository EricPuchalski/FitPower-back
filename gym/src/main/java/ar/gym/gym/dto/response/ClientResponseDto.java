package ar.gym.gym.dto.response;

import ar.gym.gym.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.Record;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientResponseDto {
    private Long id;
    private String name;
    private String lastname;
    private String dni;
    private String phone;
    private String address;
    private String email;
    private boolean active;
    private List<ClientStatus> statuses;
    private String goal;
    private String gymName;
}
