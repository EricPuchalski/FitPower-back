package ar.gym.gym.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientStatusRequestDto {
    private Double weight;
    private Double height;
    private Double bodymass;
    private Double bodyfat;

}
