package ar.gym.gym.dto.response;

import ar.gym.gym.model.Routine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerResponseDto {
    private Long id;
    private String name;
    private String lastname;
    private String dni;
    private String phone;
    private String address;
    private String email;
    private boolean active;
    private String profession;
    private List<Routine> routineList;
    private List<ClientResponseDto> clients;
    private String gymName;
}
