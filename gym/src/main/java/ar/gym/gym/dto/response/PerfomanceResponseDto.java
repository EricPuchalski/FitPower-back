package ar.gym.gym.dto.response;

import ar.gym.gym.model.Client;
import ar.gym.gym.model.PhysicalProgress;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerfomanceResponseDto {
    private Long id;
    private ClientResponseDto client;
    private List<PhysicalProgress> physicalProgresses;
}
