package ar.gym.gym.service.impl;

import ar.gym.gym.model.Client;
import ar.gym.gym.model.ClientStatus;
import ar.gym.gym.repository.ClientRepository;
import ar.gym.gym.service.ProgressEvaluationService;
import ar.gym.gym.utils.AgeCalculator;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

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


}
