package ar.gym.gym.dto.request;

import ar.gym.gym.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerRequestDto {
    private String name;
    private String surname;
    private String dni;
    private String phone;
    private String address;
    private String email;
    private boolean active;
    private String profession;
    private boolean available;
    private List<Routine> routineList;
    private List<Client>clients;
    private Gym gym;
}
