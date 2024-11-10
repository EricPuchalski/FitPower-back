package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.NutritionLogRequestDto;
import ar.gym.gym.dto.response.NutritionLogResponseDto;
import ar.gym.gym.mapper.NutritionLogMapper;
import ar.gym.gym.model.Client;
import ar.gym.gym.model.NutritionLog;
import ar.gym.gym.model.NutritionPlan;
import ar.gym.gym.repository.ClientRepository;
import ar.gym.gym.repository.NutritionLogRepository;
import ar.gym.gym.repository.NutritionPlanRepository;
import ar.gym.gym.service.NutritionLogService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NutritionLogServiceImpl implements NutritionLogService {

    private final Logger logger = LoggerFactory.getLogger(NutritionLogServiceImpl.class);
    private final NutritionLogRepository nutritionLogRepository;
    private final NutritionLogMapper nutritionLogMapper;
    private final ClientRepository clientRepository;
    private final NutritionPlanRepository nutritionPlanRepository;

    public NutritionLogServiceImpl(NutritionLogRepository nutritionLogRepository, NutritionLogMapper nutritionLogMapper, ClientRepository clientRepository, NutritionPlanRepository nutritionPlanRepository) {
        this.nutritionLogRepository = nutritionLogRepository;
        this.nutritionLogMapper = nutritionLogMapper;
        this.clientRepository = clientRepository;
        this.nutritionPlanRepository = nutritionPlanRepository;
    }

    @Override
    public NutritionLogResponseDto createNutritionLog(NutritionLogRequestDto nutritionLogRequestDto) {
        logger.info("Entering createNutritionLog method with nutrition log data: {}", nutritionLogRequestDto);
        try {
            Client client = clientRepository.findById(nutritionLogRequestDto.getClientId())
                    .orElseThrow(() -> new EntityNotFoundException("Client not found with ID: " + nutritionLogRequestDto.getClientId()));

            NutritionPlan nutritionPlan = nutritionPlanRepository.findById(nutritionLogRequestDto.getNutritionPlanId())
                    .orElseThrow(() -> new EntityNotFoundException("Nutrition plan not found with ID: " + nutritionLogRequestDto.getNutritionPlanId()));

            NutritionLog nutritionLog = nutritionLogMapper.convertToEntity(nutritionLogRequestDto);
            nutritionLog.setClient(client);
            nutritionLog.setNutritionPlan(nutritionPlan);

            NutritionLog savedNutritionLog = nutritionLogRepository.save(nutritionLog);
            NutritionLogResponseDto response = nutritionLogMapper.convertToDto(savedNutritionLog);

            logger.info("Exiting createNutritionLog method with response: {}", response);
            return response;
        } catch (EntityNotFoundException e) {
            logger.error("Error creating nutrition log: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error creating nutrition log: {}", e.getMessage());
            throw new RuntimeException("Unexpected error creating nutrition log", e);
        }
    }

    @Override
    public NutritionLogResponseDto updateNutritionLog(Long id, NutritionLogRequestDto nutritionLogRequestDto) {
        logger.info("Entering updateNutritionLog method with ID: {} and update data: {}", id, nutritionLogRequestDto);
        try {
            NutritionLog existingNutritionLog = nutritionLogRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Nutrition log not found with ID: " + id));

            if (nutritionLogRequestDto.getClientId() != null) {
                Client client = clientRepository.findById(nutritionLogRequestDto.getClientId())
                        .orElseThrow(() -> new EntityNotFoundException("Client not found with ID: " + nutritionLogRequestDto.getClientId()));
                existingNutritionLog.setClient(client);
            }
            if (nutritionLogRequestDto.getNutritionPlanId() != null) {
                NutritionPlan nutritionPlan = nutritionPlanRepository.findById(nutritionLogRequestDto.getNutritionPlanId())
                        .orElseThrow(() -> new EntityNotFoundException("Nutrition plan not found with ID: " + nutritionLogRequestDto.getNutritionPlanId()));
                existingNutritionLog.setNutritionPlan(nutritionPlan);
            }
            if (nutritionLogRequestDto.getDate() != null) {
                existingNutritionLog.setDate(nutritionLogRequestDto.getDate());
            }
         //   if (nutritionLogRequestDto.getTotalCaloriesConsumed() != null) {
         //       existingNutritionLog.setTotalCaloriesConsumed(nutritionLogRequestDto.getTotalCaloriesConsumed());
         //   }
            if (nutritionLogRequestDto.getObservations() != null) {
                existingNutritionLog.setObservations(nutritionLogRequestDto.getObservations());
            }
            existingNutritionLog.setCompleted(nutritionLogRequestDto.isCompleted());

            NutritionLog updatedNutritionLog = nutritionLogRepository.save(existingNutritionLog);
            NutritionLogResponseDto response = nutritionLogMapper.convertToDto(updatedNutritionLog);
            logger.info("Exiting updateNutritionLog method with updated nutrition log: {}", response);
            return response;
        } catch (EntityNotFoundException e) {
            logger.error("Error updating nutrition log: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error updating nutrition log: {}", e.getMessage());
            throw new RuntimeException("Unexpected error updating nutrition log", e);
        }
    }

    @Override
    public void deleteNutritionLog(Long id) {
        logger.info("Entering deleteNutritionLog method with ID: {}", id);
        try {
            NutritionLog nutritionLog = nutritionLogRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Nutrition log not found with ID: " + id));

            nutritionLogRepository.delete(nutritionLog);
            logger.info("Exiting deleteNutritionLog method with nutrition log deleted with ID: {}", id);
        } catch (EntityNotFoundException e) {
            logger.error("Error deleting nutrition log: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error deleting nutrition log: {}", e.getMessage());
            throw new RuntimeException("Unexpected error deleting nutrition log", e);
        }
    }

    @Override
    public Optional<NutritionLogResponseDto> findNutritionLogById(Long id) {
        logger.info("Entering findNutritionLogById method with ID: {}", id);
        try {
            Optional<NutritionLog> nutritionLog = nutritionLogRepository.findById(id);
            if (nutritionLog.isEmpty()) {
                throw new EntityNotFoundException("Nutrition log not found with ID: " + id);
            }
            NutritionLogResponseDto response = nutritionLogMapper.convertToDto(nutritionLog.get());
            logger.info("Exiting findNutritionLogById method with response: {}", response);
            return Optional.of(response);
        } catch (EntityNotFoundException e) {
            logger.error("Error finding nutrition log: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error finding nutrition log: {}", e.getMessage());
            throw new RuntimeException("Unexpected error finding nutrition log", e);
        }
    }

    @Override
    public List<NutritionLogResponseDto> findAllNutritionLogs() {
        logger.info("Entering findAllNutritionLogs method");
        try {
            List<NutritionLog> nutritionLogs = nutritionLogRepository.findAll();

            List<NutritionLogResponseDto> response = nutritionLogs.stream()
                    .map(nutritionLogMapper::convertToDto)
                    .collect(Collectors.toList());
            logger.info("Exiting findAllNutritionLogs method with total nutrition logs found: {}", response.size());
            return response;
        } catch (Exception e) {
            logger.error("Unexpected error fetching all nutrition logs: {}", e.getMessage());
            throw new RuntimeException("Unexpected error fetching all nutrition logs", e);
        }
    }

    @Override
    public List<NutritionLogResponseDto> findCompletedNutritionLogs() {
        logger.info("Entering findCompletedNutritionLogs method");
        try {
            List<NutritionLog> nutritionLogs = nutritionLogRepository.findByCompleted(true);

            List<NutritionLogResponseDto> response = nutritionLogs.stream()
                    .map(nutritionLogMapper::convertToDto)
                    .collect(Collectors.toList());

            logger.info("Exiting findCompletedNutritionLogs method with completed nutrition logs found: {}", response.size());
            return response;
        } catch (Exception e) {
            logger.error("Unexpected error searching completed nutrition logs: {}", e.getMessage());
            throw new RuntimeException("Unexpected error searching completed nutrition logs", e);
        }
    }

    @Override
    public List<NutritionLogResponseDto> findNutritionLogsByDate(LocalDateTime date) {
        logger.info("Entering findNutritionLogsByDate method with date: {}", date);
        try {
            List<NutritionLog> nutritionLogs = nutritionLogRepository.findByDate(date);

            List<NutritionLogResponseDto> response = nutritionLogs.stream()
                    .map(nutritionLogMapper::convertToDto)
                    .collect(Collectors.toList());

            logger.info("Exiting findNutritionLogsByDate method with nutrition logs found: {}", response.size());
            return response;
        } catch (Exception e) {
            logger.error("Unexpected error searching nutrition logs by date: {}", e.getMessage());
            throw new RuntimeException("Unexpected error searching nutrition logs by date", e);
        }
    }

    @Override
    public List<NutritionLogResponseDto> findNutritionLogsByClient(Long clientId) {
        logger.info("Entering findNutritionLogsByClient method with client ID: {}", clientId);
        try {
            List<NutritionLog> nutritionLogs = nutritionLogRepository.findByClientId(clientId);

            List<NutritionLogResponseDto> response = nutritionLogs.stream()
                    .map(nutritionLogMapper::convertToDto)
                    .collect(Collectors.toList());

            logger.info("Exiting findNutritionLogsByClient method with nutrition logs found: {}", response.size());
            return response;
        } catch (Exception e) {
            logger.error("Unexpected error searching nutrition logs by client: {}", e.getMessage());
            throw new RuntimeException("Unexpected error searching nutrition logs by client", e);
        }
    }

    @Override
    public List<NutritionLogResponseDto> findNutritionLogsByNutritionPlan(Long nutritionPlanId) {
        logger.info("Entering findNutritionLogsByNutritionPlan method with nutrition plan ID: {}", nutritionPlanId);
        try {
            List<NutritionLog> nutritionLogs = nutritionLogRepository.findByNutritionPlanId(nutritionPlanId);

            List<NutritionLogResponseDto> response = nutritionLogs.stream()
                    .map(nutritionLogMapper::convertToDto)
                    .collect(Collectors.toList());

            logger.info("Exiting findNutritionLogsByNutritionPlan method with nutrition logs found: {}", response.size());
            return response;
        } catch (Exception e) {
            logger.error("Unexpected error searching nutrition logs by nutrition plan: {}", e.getMessage());
            throw new RuntimeException("Unexpected error searching nutrition logs by nutrition plan", e);
        }
    }
}
