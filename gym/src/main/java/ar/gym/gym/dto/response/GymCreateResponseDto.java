package ar.gym.gym.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GymCreateResponseDto {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;
}
