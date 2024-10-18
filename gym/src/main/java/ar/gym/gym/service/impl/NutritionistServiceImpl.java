package ar.gym.gym.service.impl;

import com.itec.FitFlowApp.dto.request.NutritionistRequestDto;
import com.itec.FitFlowApp.dto.response.ClientResponseDto;
import com.itec.FitFlowApp.dto.response.NutritionistResponseDto;
import com.itec.FitFlowApp.exeption.EntityException;
import com.itec.FitFlowApp.mapper.ClientMapper;
import com.itec.FitFlowApp.mapper.NutritionistMapper;
import com.itec.FitFlowApp.model.entity.Nutritionist;
import com.itec.FitFlowApp.model.repository.NutritionistRepository;
import com.itec.FitFlowApp.util.CRUD;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class NutritionistServiceImpl implements CRUD<NutritionistResponseDto, NutritionistRequestDto> {
    private NutritionistRepository nutritionistRepository;
    private NutritionistMapper nutritionistMapper;
    private ClientMapper clientMapper;

    @Override
    public NutritionistResponseDto create(NutritionistRequestDto nutritionistRequestDto) {
        if(nutritionistRepository.findByDni(nutritionistRequestDto.getDni()).isPresent()){
            throw new EntityException("Ya existe un nutricionista con el DNI " + nutritionistRequestDto.getDni());
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
                .orElseThrow(() -> new EntityException("El nutricionista con el DNI " + dni + " no existe"));
    }

    @Override
    public NutritionistResponseDto findById(String id) {
        Nutritionist nutritionist = getNutritionistByDniOrThrow(id);
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
        existingNutritionist.setAvailable(nutritionistRequestDto.isAvailable());

        // Guardamos el entrenador actualizado en la base de datos
        Nutritionist updatedNutritionist = nutritionistRepository.save(existingNutritionist);

        // Devolvemos el DTO actualizado usando el mapper
        return nutritionistMapper.entityToDto(updatedNutritionist);
    }

    @Override
    public void delete(String id) {
        Nutritionist nutritionist = new Nutritionist();
        nutritionistRepository.delete(nutritionist);
    }

    public List<ClientResponseDto> getClients(String dni){
        Nutritionist nutritionist = getNutritionistByDniOrThrow(dni);
        return nutritionist.getClients()
                .stream()
                .map(clientMapper::entityToDto)
                .collect(Collectors.toList());
    }

}
