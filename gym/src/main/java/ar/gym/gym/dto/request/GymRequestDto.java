package ar.gym.gym.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GymRequestDto {
    private String name;
    private String phone;
    private String email;
    private String address;
}
