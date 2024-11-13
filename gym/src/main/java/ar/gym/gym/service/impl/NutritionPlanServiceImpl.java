package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.NutritionLogRequestDto;
import ar.gym.gym.dto.request.NutritionPlanRequestDto;
import ar.gym.gym.dto.response.NutritionLogResponseDto;
import ar.gym.gym.dto.response.NutritionPlanResponseDto;
import ar.gym.gym.mapper.NutritionLogMapper;
import ar.gym.gym.mapper.NutritionPlanMapper;
import ar.gym.gym.model.*;
import ar.gym.gym.repository.ClientRepository;
import ar.gym.gym.repository.NutritionLogRepository;
import ar.gym.gym.repository.NutritionPlanRepository;
import ar.gym.gym.repository.NutritionistRepository;
import ar.gym.gym.service.NutritionPlanService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NutritionPlanServiceImpl implements NutritionPlanService {

    private final Logger logger = LoggerFactory.getLogger(NutritionPlanServiceImpl.class);
    private final NutritionPlanRepository nutritionPlanRepository;
    private final NutritionPlanMapper nutritionPlanMapper;
    private final NutritionLogMapper nutritionLogMapper;
    private final ClientRepository clientRepository;
    private final NutritionistRepository nutritionistRepository;
    private final NutritionLogRepository nutritionLogRepository;

    public NutritionPlanServiceImpl(NutritionPlanRepository nutritionPlanRepository,
                                    NutritionPlanMapper nutritionPlanMapper,
                                    NutritionLogMapper nutritionLogMapper,
                                    ClientRepository clientRepository,
                                    NutritionistRepository nutritionistRepository,
                                    NutritionLogRepository nutritionLogRepository) {
        this.nutritionPlanRepository = nutritionPlanRepository;
        this.nutritionPlanMapper = nutritionPlanMapper;
        this.nutritionLogMapper = nutritionLogMapper;
        this.clientRepository = clientRepository;
        this.nutritionistRepository = nutritionistRepository;
        this.nutritionLogRepository = nutritionLogRepository;
    }

    @Override
    public NutritionPlanResponseDto createNutritionPlan(NutritionPlanRequestDto nutritionPlanRequestDto) {
        logger.info("Entering createNutritionPlan method with nutrition plan data: {}", nutritionPlanRequestDto);

        try {
            if (nutritionPlanRequestDto.getClientDni() == null || nutritionPlanRequestDto.getNutritionistDni() == null) {
                throw new IllegalArgumentException("Client DNI and Nutritionist DNI cannot be null");
            }

            Client client = clientRepository.findByDni(nutritionPlanRequestDto.getClientDni())
                    .orElseThrow(() -> new IllegalArgumentException("Client not found with DNI: " + nutritionPlanRequestDto.getClientDni()));

            Nutritionist nutritionist = nutritionistRepository.findByDni(nutritionPlanRequestDto.getNutritionistDni())
                    .orElseThrow(() -> new IllegalArgumentException("Nutritionist not found with DNI: " + nutritionPlanRequestDto.getNutritionistDni()));

            NutritionPlan nutritionPlan = nutritionPlanMapper.convertToEntity(nutritionPlanRequestDto);
            nutritionPlan.setClient(client);
            nutritionPlan.setNutritionist(nutritionist);
            nutritionPlan.setActive(false);

            NutritionPlan savedNutritionPlan = nutritionPlanRepository.save(nutritionPlan);

            NutritionPlanResponseDto response = nutritionPlanMapper.convertToDto(savedNutritionPlan);

            logger.info("Exiting createNutritionPlan method with response: {}", response);
            return response;
        } catch (IllegalArgumentException e) {
            logger.error("Error creating nutrition plan: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error creating nutrition plan: {}", e.getMessage());
            throw new RuntimeException("Unexpected error creating nutrition plan", e);
        }
    }


    @Override
    public NutritionPlanResponseDto updateNutritionPlan(Long id, NutritionPlanRequestDto nutritionPlanRequestDto) {
        logger.info("Entering updateNutritionPlan method with ID: {} and update data: {}", id, nutritionPlanRequestDto);
        try {
            NutritionPlan existingNutritionPlan = nutritionPlanRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Nutrition plan not found with ID: " + id));

            if (nutritionPlanRequestDto.getClientDni() != null) {
                Client client = clientRepository.findByDni(nutritionPlanRequestDto.getClientDni())
                        .orElseThrow(() -> new EntityNotFoundException("Client not found with ID: " + nutritionPlanRequestDto.getClientDni()));
                existingNutritionPlan.setClient(client);
            }
            if (nutritionPlanRequestDto.getNutritionistDni() != null) {
                Nutritionist nutritionist = nutritionistRepository.findByDni(nutritionPlanRequestDto.getNutritionistDni())
                        .orElseThrow(() -> new EntityNotFoundException("Nutritionist not found with ID: " + nutritionPlanRequestDto.getNutritionistDni()));
                existingNutritionPlan.setNutritionist(nutritionist);
            }
            if (nutritionPlanRequestDto.getType() != null) {
                existingNutritionPlan.setType(nutritionPlanRequestDto.getType());
            }
            if (nutritionPlanRequestDto.getStartDate() != null) {
                existingNutritionPlan.setStartDate(nutritionPlanRequestDto.getStartDate());
            }
            if (nutritionPlanRequestDto.getEndDate() != null) {
                existingNutritionPlan.setEndDate(nutritionPlanRequestDto.getEndDate());
            }
            if (nutritionPlanRequestDto.getDescription() != null) {
                existingNutritionPlan.setDescription(nutritionPlanRequestDto.getDescription());
            }
            if (nutritionPlanRequestDto.getStatus() != null) {
                existingNutritionPlan.setStatus(nutritionPlanRequestDto.getStatus());
            }
            existingNutritionPlan.setCompleted(nutritionPlanRequestDto.isCompleted());

            NutritionPlan updatedNutritionPlan = nutritionPlanRepository.save(existingNutritionPlan);
            NutritionPlanResponseDto response = nutritionPlanMapper.convertToDto(updatedNutritionPlan);
            logger.info("Exiting updateNutritionPlan method with updated nutrition plan: {}", response);
            return response;
        } catch (EntityNotFoundException e) {
            logger.error("Error updating nutrition plan: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error updating nutrition plan: {}", e.getMessage());
            throw new RuntimeException("Unexpected error updating nutrition plan", e);
        }
    }

    @Override
    public void deleteNutritionPlan(Long id) {
        logger.info("Entering deleteNutritionPlan method with ID: {}", id);
        try {
            NutritionPlan nutritionPlan = nutritionPlanRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Nutrition plan not found with ID: " + id));

            nutritionPlanRepository.delete(nutritionPlan);
            logger.info("Exiting deleteNutritionPlan method with nutrition plan deleted with ID: {}", id);
        } catch (EntityNotFoundException e) {
            logger.error("Error deleting nutrition plan: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error deleting nutrition plan: {}", e.getMessage());
            throw new RuntimeException("Unexpected error deleting nutrition plan", e);
        }
    }

    @Override
    public Optional<NutritionPlanResponseDto> findNutritionPlanById(Long id) {
        logger.info("Entering findNutritionPlanById method with ID: {}", id);
        try {
            Optional<NutritionPlan> nutritionPlan = nutritionPlanRepository.findById(id);
            if (nutritionPlan.isEmpty()) {
                throw new EntityNotFoundException("Nutrition plan not found with ID: " + id);
            }
            NutritionPlanResponseDto response = nutritionPlanMapper.convertToDto(nutritionPlan.get());
            logger.info("Exiting findNutritionPlanById method with response: {}", response);
            return Optional.of(response);
        } catch (EntityNotFoundException e) {
            logger.error("Error finding nutrition plan: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error finding nutrition plan: {}", e.getMessage());
            throw new RuntimeException("Unexpected error finding nutrition plan", e);
        }
    }

    @Override
    public List<NutritionPlanResponseDto> findAllNutritionPlans() {
        logger.info("Entering findAllNutritionPlans method");
        try {
            List<NutritionPlan> nutritionPlans = nutritionPlanRepository.findAll();

            List<NutritionPlanResponseDto> response = nutritionPlans.stream()
                    .map(nutritionPlanMapper::convertToDto)
                    .collect(Collectors.toList());
            logger.info("Exiting findAllNutritionPlans method with total nutrition plans found: {}", response.size());
            return response;
        } catch (Exception e) {
            logger.error("Unexpected error fetching all nutrition plans: {}", e.getMessage());
            throw new RuntimeException("Unexpected error fetching all nutrition plans", e);
        }
    }

    @Override
    public List<NutritionPlanResponseDto> findCompletedNutritionPlans() {
        logger.info("Entering findCompletedNutritionPlans method");
        try {
            List<NutritionPlan> nutritionPlans = nutritionPlanRepository.findByCompleted(true);

            List<NutritionPlanResponseDto> response = nutritionPlans.stream()
                    .map(nutritionPlanMapper::convertToDto)
                    .collect(Collectors.toList());

            logger.info("Exiting findCompletedNutritionPlans method with completed nutrition plans found: {}", response.size());
            return response;
        } catch (Exception e) {
            logger.error("Unexpected error searching completed nutrition plans: {}", e.getMessage());
            throw new RuntimeException("Unexpected error searching completed nutrition plans", e);
        }
    }

    @Override
    public List<NutritionPlanResponseDto> findNutritionPlansByClient(Long clientId) {
        logger.info("Entering findNutritionPlansByClient method with client ID: {}", clientId);
        try {
            List<NutritionPlan> nutritionPlans = nutritionPlanRepository.findByClientId(clientId);

            List<NutritionPlanResponseDto> response = nutritionPlans.stream()
                    .map(nutritionPlanMapper::convertToDto)
                    .collect(Collectors.toList());

            logger.info("Exiting findNutritionPlansByClient method with nutrition plans found: {}", response.size());
            return response;
        } catch (Exception e) {
            logger.error("Unexpected error searching nutrition plans by client: {}", e.getMessage());
            throw new RuntimeException("Unexpected error searching nutrition plans by client", e);
        }
    }

    @Override
    public List<NutritionPlanResponseDto> findNutritionPlansByNutritionist(Long nutritionistId) {
        logger.info("Entering findNutritionPlansByNutritionist method with nutritionist ID: {}", nutritionistId);
        try {
            List<NutritionPlan> nutritionPlans = nutritionPlanRepository.findByNutritionistId(nutritionistId);

            List<NutritionPlanResponseDto> response = nutritionPlans.stream()
                    .map(nutritionPlanMapper::convertToDto)
                    .collect(Collectors.toList());

            logger.info("Exiting findNutritionPlansByNutritionist method with nutrition plans found: {}", response.size());
            return response;
        } catch (Exception e) {
            logger.error("Unexpected error searching nutrition plans by nutritionist: {}", e.getMessage());
            throw new RuntimeException("Unexpected error searching nutrition plans by nutritionist", e);
        }
    }

    //LÓGICA PARA AGREGAR LOS NUTRITIONLOGS AL NUTRITION PLAN
    @Override
    public NutritionLogResponseDto addNutritionLogToNutritionPlan(Long nutritionPlanId, NutritionLogRequestDto nutritionLogRequestDto) {
        logger.info("Entering addNutritionLogToNutritionPlan method with nutrition plan ID: {} and nutrition log data: {}", nutritionPlanId, nutritionLogRequestDto);
        try {
            NutritionPlan nutritionPlan = nutritionPlanRepository.findById(nutritionPlanId)
                    .orElseThrow(() -> new EntityNotFoundException("Nutrition plan not found with ID: " + nutritionPlanId));

            NutritionLog nutritionLog = nutritionLogMapper.convertToEntity(nutritionLogRequestDto);
            nutritionLog.setNutritionPlan(nutritionPlan);

            NutritionLog savedNutritionLog = nutritionLogRepository.save(nutritionLog);
            NutritionLogResponseDto response = nutritionLogMapper.convertToDto(savedNutritionLog);

            logger.info("Exiting addNutritionLogToNutritionPlan method with response: {}", response);
            return response;
        } catch (EntityNotFoundException e) {
            logger.error("Error adding nutrition log to nutrition plan: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error adding nutrition log to nutrition plan: {}", e.getMessage());
            throw new RuntimeException("Unexpected error adding nutrition log to nutrition plan", e);
        }
    }

    @Override
    public NutritionLogResponseDto updateNutritionLogInNutritionPlan(Long nutritionPlanId, Long nutritionLogId, NutritionLogRequestDto nutritionLogRequestDto) {
        logger.info("Entering updateNutritionLogInNutritionPlan method with nutrition plan ID: {}, nutrition log ID: {} and update data: {}", nutritionPlanId, nutritionLogId, nutritionLogRequestDto);
        try {
            NutritionPlan nutritionPlan = nutritionPlanRepository.findById(nutritionPlanId)
                    .orElseThrow(() -> new EntityNotFoundException("Nutrition plan not found with ID: " + nutritionPlanId));

            NutritionLog existingNutritionLog = nutritionLogRepository.findById(nutritionLogId)
                    .orElseThrow(() -> new EntityNotFoundException("Nutrition log not found with ID: " + nutritionLogId));

            if (!existingNutritionLog.getNutritionPlan().getId().equals(nutritionPlanId)) {
                throw new IllegalArgumentException("Nutrition log does not belong to the specified nutrition plan");
            }

            if (nutritionLogRequestDto.getClientDni() != null) {
                Client client = clientRepository.findByDni(nutritionLogRequestDto.getClientDni())
                        .orElseThrow(() -> new EntityNotFoundException("Client not found with ID: " + nutritionLogRequestDto.getClientDni()));
                existingNutritionLog.setClient(client);
            }
            if (nutritionLogRequestDto.getDate() != null) {
                existingNutritionLog.setDate(nutritionLogRequestDto.getDate());
            }
            if (nutritionLogRequestDto.getObservations() != null) {
                existingNutritionLog.setObservations(nutritionLogRequestDto.getObservations());
            }
            existingNutritionLog.setCompleted(nutritionLogRequestDto.isCompleted());

            NutritionLog updatedNutritionLog = nutritionLogRepository.save(existingNutritionLog);
            NutritionLogResponseDto response = nutritionLogMapper.convertToDto(updatedNutritionLog);

            logger.info("Exiting updateNutritionLogInNutritionPlan method with response: {}", response);
            return response;
        } catch (EntityNotFoundException e) {
            logger.error("Error updating nutrition log in nutrition plan: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error updating nutrition log in nutrition plan: {}", e.getMessage());
            throw new RuntimeException("Unexpected error updating nutrition log in nutrition plan", e);
        }
    }

    @Override
    public void deleteNutritionLogFromNutritionPlan(Long nutritionPlanId, Long nutritionLogId) {
        logger.info("Entering deleteNutritionLogFromNutritionPlan method with nutrition plan ID: {} and nutrition log ID: {}", nutritionPlanId, nutritionLogId);
        try {
            NutritionPlan nutritionPlan = nutritionPlanRepository.findById(nutritionPlanId)
                    .orElseThrow(() -> new EntityNotFoundException("Nutrition plan not found with ID: " + nutritionPlanId));

            NutritionLog nutritionLog = nutritionLogRepository.findById(nutritionLogId)
                    .orElseThrow(() -> new EntityNotFoundException("Nutrition log not found with ID: " + nutritionLogId));

            if (!nutritionLog.getNutritionPlan().getId().equals(nutritionPlanId)) {
                throw new IllegalArgumentException("Nutrition log does not belong to the specified nutrition plan");
            }

            nutritionLogRepository.delete(nutritionLog);
            logger.info("Exiting deleteNutritionLogFromNutritionPlan method with nutrition log deleted with ID: {}", nutritionLogId);
        } catch (EntityNotFoundException e) {
            logger.error("Error deleting nutrition log from nutrition plan: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error deleting nutrition log from nutrition plan: {}", e.getMessage());
            throw new RuntimeException("Unexpected error deleting nutrition log from nutrition plan", e);
        }
    }

}
