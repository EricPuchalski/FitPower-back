package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.NutritionistRequestDto;
import ar.gym.gym.dto.response.ClientResponseDto;
import ar.gym.gym.dto.response.NutritionistResponseDto;
import ar.gym.gym.mapper.ClientMapper;
import ar.gym.gym.mapper.NutritionistMapper;
import ar.gym.gym.model.Gym;
import ar.gym.gym.model.Nutritionist;
import ar.gym.gym.repository.GymRepository;
import ar.gym.gym.repository.NutritionistRepository;
import ar.gym.gym.service.NutritionistService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NutritionistServiceImpl implements NutritionistService {
    private static final Logger logger = LoggerFactory.getLogger(NutritionistServiceImpl.class);

    private final NutritionistRepository nutritionistRepository;
    private final NutritionistMapper nutritionistMapper;
    private final GymRepository gymRepository;
    private final ClientMapper clientMapper;

    public NutritionistServiceImpl(NutritionistRepository nutritionistRepository, NutritionistMapper nutritionistMapper, GymRepository gymRepository, ClientMapper clientMapper) {
        this.nutritionistRepository = nutritionistRepository;
        this.nutritionistMapper = nutritionistMapper;
        this.gymRepository = gymRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    public NutritionistResponseDto create(NutritionistRequestDto nutritionistRequestDto) {
        logger.info("Creating nutritionist with DNI: {}", nutritionistRequestDto.getDni());

        if (nutritionistRepository.findByDni(nutritionistRequestDto.getDni()).isPresent()) {
            logger.error("Nutritionist with DNI {} already exists", nutritionistRequestDto.getDni());
            throw new EntityExistsException("Ya existe un nutricionista con el DNI " + nutritionistRequestDto.getDni());
        }

        Nutritionist nutritionist = nutritionistMapper.dtoToEntity(nutritionistRequestDto);

        if (nutritionistRequestDto.getGymName() != null) {
            Optional<Gym> gym = gymRepository.findByName(nutritionistRequestDto.getGymName());

            if (gym.isPresent()) {
                nutritionist.setGym(gym.get());
            } else {
                logger.error("Gym not found with name: {}", nutritionistRequestDto.getGymName());
                throw new EntityNotFoundException("Gimnasio no encontrado con el nombre: " + nutritionistRequestDto.getGymName());
            }
        }
        nutritionist.setActive(true);
        nutritionistRepository.save(nutritionist);

        logger.info("Nutritionist created successfully: {}", nutritionistRequestDto.getDni());
        return nutritionistMapper.entityToDto(nutritionist);
    }

    @Override
    public List<NutritionistResponseDto> findAll() {
        logger.info("Fetching all nutritionists");
        List<Nutritionist> nutritionists = nutritionistRepository.findAll();
        return nutritionists.stream()
                .map(nutritionistMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public Nutritionist getNutritionistByDniOrThrow(String dni) {
        logger.info("Fetching nutritionist with DNI: {}", dni);
        return nutritionistRepository.findByDni(dni)
                .orElseThrow(() -> {
                    logger.error("Nutritionist with DNI {} does not exist", dni);
                    return new EntityExistsException("El nutricionista con el DNI " + dni + " no existe");
                });
    }

    @Override
    public NutritionistResponseDto findByDni(String dni) {
        logger.info("Finding nutritionist by DNI: {}", dni);
        Nutritionist nutritionist = getNutritionistByDniOrThrow(dni);
        return nutritionistMapper.entityToDto(nutritionist);
    }

    @Override
    public NutritionistResponseDto update(NutritionistRequestDto nutritionistRequestDto) {
        logger.info("Updating nutritionist with DNI: {}", nutritionistRequestDto.getDni());
        Nutritionist existingNutritionist = getNutritionistByDniOrThrow(nutritionistRequestDto.getDni());

        if (nutritionistRequestDto.getName() != null && !nutritionistRequestDto.getName().isEmpty()) {
            existingNutritionist.setName(nutritionistRequestDto.getName());
        }
        if (nutritionistRequestDto.getLastname() != null && !nutritionistRequestDto.getLastname().isEmpty()) {
            existingNutritionist.setLastname(nutritionistRequestDto.getLastname());
        }
        if (nutritionistRequestDto.getPhone() != null && !nutritionistRequestDto.getPhone().isEmpty()) {
            existingNutritionist.setPhone(nutritionistRequestDto.getPhone());
        }
        if (nutritionistRequestDto.getAddress() != null && !nutritionistRequestDto.getAddress().isEmpty()) {
            existingNutritionist.setAddress(nutritionistRequestDto.getAddress());
        }
        if (nutritionistRequestDto.getEmail() != null && !nutritionistRequestDto.getEmail().isEmpty()) {
            existingNutritionist.setEmail(nutritionistRequestDto.getEmail());
        }
        if (nutritionistRequestDto.getProfession() != null && !nutritionistRequestDto.getProfession().isEmpty()) {
            existingNutritionist.setProfession(nutritionistRequestDto.getProfession());
        }
        if (nutritionistRequestDto.getGymName() != null) {
            Optional<Gym> gym = gymRepository.findByName(nutritionistRequestDto.getGymName());

            if (gym.isPresent()) {
                existingNutritionist.setGym(gym.get());
            } else {
                logger.error("Gym not found with name: {}", nutritionistRequestDto.getGymName());
                throw new EntityNotFoundException("Gimnasio no encontrado con el nombre: " + nutritionistRequestDto.getGymName());
            }
        }

        existingNutritionist.setActive(nutritionistRequestDto.isActive());

        Nutritionist updatedNutritionist = nutritionistRepository.save(existingNutritionist);
        logger.info("Nutritionist updated successfully: {}", nutritionistRequestDto.getDni());

        return nutritionistMapper.entityToDto(updatedNutritionist);
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting nutritionist with ID: {}", id);
        nutritionistRepository.deleteById(id);
    }

    public List<ClientResponseDto> getClientsAssociated(String dni) {
        logger.info("Fetching clients associated with nutritionist DNI: {}", dni);
        Nutritionist nutritionist = getNutritionistByDniOrThrow(dni);
        return nutritionist.getClients()
                .stream()
                .map(clientMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public NutritionistResponseDto disableNutritionistByDni(String dni) {
        logger.info("Disabling nutritionist with DNI: {}", dni);
        Nutritionist nutritionist = nutritionistRepository.findByDni(dni)
                .orElseThrow(() -> {
                    logger.error("Nutritionist not found with DNI: {}", dni);
                    return new EntityNotFoundException("Nutricionista no encontrado con DNI: " + dni);
                });

        nutritionist.setActive(false);
        nutritionistRepository.save(nutritionist);
        logger.info("Nutritionist disabled successfully: {}", dni);
        return nutritionistMapper.entityToDto(nutritionist);
    }
}
