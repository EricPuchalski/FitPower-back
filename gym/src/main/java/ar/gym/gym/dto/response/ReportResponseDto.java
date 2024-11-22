package ar.gym.gym.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDto {
    private List<Double> bmiValues;
    private List<Double> muscleMassValues;
    private List<Double> bodyFatValues;
}
