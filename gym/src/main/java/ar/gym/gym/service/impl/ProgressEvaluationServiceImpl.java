package ar.gym.gym.service.impl;

import ar.gym.gym.dto.response.PerfomanceResponseDto;
import ar.gym.gym.dto.response.ReportResponseDto;
import ar.gym.gym.model.Client;
import ar.gym.gym.model.ClientStatus;
import ar.gym.gym.model.PhysicalProgress;
import ar.gym.gym.repository.ClientRepository;
import ar.gym.gym.service.ProgressEvaluationService;
import ar.gym.gym.utils.AgeCalculator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProgressEvaluationServiceImpl implements ProgressEvaluationService {

    private final ClientRepository clientRepository;

    public ProgressEvaluationServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public double calculateIMC(String dni) {
        Client client = getClientByDni(dni);
        ClientStatus latestStatus = getLatestClientStatus(client);
        double heightInMeters = latestStatus.getHeight() / 100.0; // Convertir altura a metros
        return latestStatus.getWeight() / (heightInMeters * heightInMeters);
    }

    @Override
    public double calculateBodyFatPercentage(String dni) {
        Client client = getClientByDni(dni);
        double imc = calculateIMC(dni);
        int age = AgeCalculator.calculateAge(client.getBirthDate());
        if (client.getGender().equalsIgnoreCase("hombre")) {
            return 1.20 * imc + 0.23 * age - 16.2;
        } else {
            return 1.20 * imc + 0.23 * age - 5.4;
        }
    }

    @Override
    public double calculateMuscleMass(String dni) {
        Client client = getClientByDni(dni);
        double bodyFatPercentage = calculateBodyFatPercentage(dni);
        ClientStatus latestStatus = getLatestClientStatus(client);
        return latestStatus.getWeight() - (latestStatus.getWeight() * bodyFatPercentage / 100);
    }

    private Client getClientByDni(String dni) {
        return clientRepository.findByDni(dni)
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el cliente con el DNI: " + dni));
    }

    private ClientStatus getLatestClientStatus(Client client) {
        return client.getStatuses().stream()
                .sorted((s1, s2) -> Long.compare(s2.getId(), s1.getId())) // Ordena por ID descendente
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("No se encontró ningún estado para el cliente"));
    }
    public ReportResponseDto generateReportFromPerformance(PerfomanceResponseDto performance) {
        List<PhysicalProgress> progresses = performance.getPhysicalProgresses();
        // Ordenar por fecha descendente y tomar los últimos 4 registros
        progresses.sort(Comparator.comparing(PhysicalProgress::getDate).reversed());
        List<PhysicalProgress> lastFourProgresses = progresses.subList(0, Math.min(4, progresses.size()));

        // Extraer los valores de BMI, masa muscular y grasa corporal
        List<Double> bmiValues = lastFourProgresses.stream().map(PhysicalProgress::getBmi).collect(Collectors.toList());
        List<Double> muscleMassValues = lastFourProgresses.stream().map(PhysicalProgress::getMuscleMass).collect(Collectors.toList());
        List<Double> bodyFatValues = lastFourProgresses.stream().map(PhysicalProgress::getBodyFat).collect(Collectors.toList());

        // Generar el informe
        ReportResponseDto report = new ReportResponseDto();
        report.setBmiValues(bmiValues);
        report.setMuscleMassValues(muscleMassValues);
        report.setBodyFatValues(bodyFatValues);
        return report;
    }

}
