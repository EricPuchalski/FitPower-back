package ar.gym.gym.service;


import ar.gym.gym.dto.response.PerfomanceResponseDto;
import ar.gym.gym.dto.response.ReportResponseDto;

public interface ProgressEvaluationService {
     double calculateIMC(String dni) ;

    double calculateBodyFatPercentage(String dni);

     double calculateMuscleMass(String dni);
    ReportResponseDto generateReportFromPerformance(PerfomanceResponseDto performance);
}
