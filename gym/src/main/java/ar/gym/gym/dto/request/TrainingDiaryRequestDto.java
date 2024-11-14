package ar.gym.gym.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingDiaryRequestDto {
    private SessionRequestDto session;
    @JsonProperty("clientDni")
    private String clientDni;
    @JsonProperty("observation")
    private String observation;
}
