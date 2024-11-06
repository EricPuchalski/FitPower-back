package ar.gym.gym.dto.request;

import ar.gym.gym.model.Client;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
