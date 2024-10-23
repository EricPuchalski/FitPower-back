package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.NutritionistRequestDto;
import ar.gym.gym.dto.response.ClientResponseDto;
import ar.gym.gym.dto.response.NutritionistResponseDto;
import ar.gym.gym.mapper.ClientMapper;
import ar.gym.gym.mapper.NutritionistMapper;
import ar.gym.gym.model.Nutritionist;
import ar.gym.gym.repository.NutritionistRepository;
import ar.gym.gym.service.NutritionistService;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class NutritionistServiceImpl implements NutritionistService {
    private NutritionistRepository nutritionistRepository;
    private NutritionistMapper nutritionistMapper;
    private ClientMapper clientMapper;

    @Override
    public NutritionistResponseDto create(NutritionistRequestDto nutritionistRequestDto) {
        if(nutritionistRepository.findByDni(nutritionistRequestDto.getDni()).isPresent()){
            throw new EntityExistsException("Ya existe un nutricionista con el DNI " + nutritionistRequestDto.getDni());
        }
        Nutritionist nutritionist = nutritionistMapper.dtoToEntity(nutritionistRequestDto);
        nutritionistRepository.save(nutritionist);
        return nutritionistMapper.entityToDto(nutritionist);
    }

    @Override
    public List<NutritionistResponseDto> findAll() {
        List<Nutritionist> nutritionists = nutritionistRepository.findAll();
        return nutritionists.stream()
                .map(nutritionistMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public Nutritionist getNutritionistByDniOrThrow(String dni) {
        return nutritionistRepository.findByDni(dni)
                .orElseThrow(() -> new EntityExistsException("El nutricionista con el DNI " + dni + " no existe"));
    }

    @Override
    public NutritionistResponseDto findByDni(String dni) {
        Nutritionist nutritionist = getNutritionistByDniOrThrow(dni);
        return nutritionistMapper.entityToDto(nutritionist);
    }

    @Override
    public NutritionistResponseDto update(NutritionistRequestDto nutritionistRequestDto) {
        Nutritionist existingNutritionist = getNutritionistByDniOrThrow(nutritionistRequestDto.getDni());

        if (nutritionistRequestDto.getName() != null && !nutritionistRequestDto.getName().isEmpty()) {
            existingNutritionist.setName(nutritionistRequestDto.getName());
        }
        if (nutritionistRequestDto.getSurname() != null && !nutritionistRequestDto.getSurname().isEmpty()) {
            existingNutritionist.setSurname(nutritionistRequestDto.getSurname());
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

        existingNutritionist.setActive(nutritionistRequestDto.isActive());

        // Guardamos el entrenador actualizado en la base de datos
        Nutritionist updatedNutritionist = nutritionistRepository.save(existingNutritionist);

        // Devolvemos el DTO actualizado usando el mapper
        return nutritionistMapper.entityToDto(updatedNutritionist);
    }

    @Override
    public void delete(Long id) {
        nutritionistRepository.deleteById(id);
    }

    public List<ClientResponseDto> getClientsAssociated(String dni){
        Nutritionist nutritionist = getNutritionistByDniOrThrow(dni);
        return nutritionist.getClients()
                .stream()
                .map(clientMapper::entityToDto)
                .collect(Collectors.toList());
    }

}
