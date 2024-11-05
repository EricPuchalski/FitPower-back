package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.TrainingPlanRequestDto;
import ar.gym.gym.dto.response.RoutineResponseDto;
import ar.gym.gym.dto.response.TrainingPlanResponseDto;
import ar.gym.gym.mapper.RoutineMapper;
import ar.gym.gym.model.Client;
import ar.gym.gym.model.Routine;
import ar.gym.gym.model.TrainingPlan;
import ar.gym.gym.repository.ClientRepository;
import ar.gym.gym.service.TrainingPlanService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainingPlanServiceImpl implements TrainingPlanService {
    private final ClientRepository clientRepository;
    private final RoutineMapper routineMapper;

    public TrainingPlanServiceImpl(ClientRepository clientRepository, RoutineMapper routineMapper) {
        this.clientRepository = clientRepository;
        this.routineMapper = routineMapper;
    }

    public void completeCurrentRoutine(String dni, Long trainingPlanId) {
        // Encuentra el TrainingPlan correspondiente al cliente
        Optional<Client> client = clientRepository.findByDni(dni);
        TrainingPlan trainingPlan;
        if (client.isPresent()){
            trainingPlan = client.get().getTrainingPlans().stream()
                    .filter(plan -> plan.getId().equals(trainingPlanId))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Training plan no encontrado"));

            Routine activeRoutine = trainingPlan.getRoutines().get(trainingPlan.getActiveRoutineIndex());
            // Obtiene la rutina activa actual
            activeRoutine.setActive(false); // Marca como completada

            // Determina el índice de la próxima rutina a activar
            int nextRoutineIndex = (trainingPlan.getActiveRoutineIndex() + 1) % trainingPlan.getRoutines().size();
            trainingPlan.setActiveRoutineIndex(nextRoutineIndex);
            Routine nextRoutine = trainingPlan.getRoutines().get(nextRoutineIndex);
            // Activa la siguiente rutina en la lista

            nextRoutine.setActive(true);

            // Guarda los cambios en la base de datos
            clientRepository.save(client.get());
        }

        throw new EntityNotFoundException("No existe el cliente con el dni: " + dni);
    }

    public RoutineResponseDto getCurrentActiveRoutine(String clientDni, Long trainingPlanId) {
        // Encuentra el TrainingPlan correspondiente
        Optional<Client> client = clientRepository.findByDni(clientDni);
        if (client.isPresent()){
            TrainingPlan trainingPlan = client.get().getTrainingPlans().stream()
                    .filter(plan -> plan.getId().equals(trainingPlanId))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Training plan no encontrado"));

            // Retorna la rutina activa

            return routineMapper.entityToDto(trainingPlan.getRoutines().get(trainingPlan.getActiveRoutineIndex()));
        }
        throw new EntityNotFoundException("No existe el cliente con el dni: " + clientDni);

    }
    @Override
    public TrainingPlanResponseDto create(TrainingPlanRequestDto trainingPlanRequestDto) {
        return null;
    }

    @Override
    public List<TrainingPlanResponseDto> findAll() {
        return List.of();
    }

    @Override
    public TrainingPlanResponseDto findByDni(String dni) {
        return null;
    }

    @Override
    public TrainingPlanResponseDto update(TrainingPlanRequestDto trainingPlanRequestDto, Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
