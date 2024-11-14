package ar.gym.gym.mapper;

import ar.gym.gym.dto.response.MealDetailResponseDto;
import ar.gym.gym.model.MealDetail;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MealDetailMapper {

    private final ModelMapper modelMapper;

    public MealDetailMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Convertir un MealDetail a MealDetailResponseDto
    public MealDetailResponseDto convertToDto(MealDetail mealDetail) {
        return modelMapper.map(mealDetail, MealDetailResponseDto.class);
    }

    // Convertir un MealDetailResponseDto a MealDetail
    public MealDetail convertToEntity(MealDetailResponseDto mealDetailResponseDto) {
        return modelMapper.map(mealDetailResponseDto, MealDetail.class);
    }

    // Convertir una lista de MealDetails a una lista de MealDetailResponseDto
    public List<MealDetailResponseDto> convertToDtoList(List<MealDetail> mealDetails) {
        return mealDetails.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Convertir una lista de MealDetailResponseDto a una lista de MealDetail
    public List<MealDetail> convertToEntityList(List<MealDetailResponseDto> mealDetailResponseDtos) {
        return mealDetailResponseDtos.stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList());
    }
}
