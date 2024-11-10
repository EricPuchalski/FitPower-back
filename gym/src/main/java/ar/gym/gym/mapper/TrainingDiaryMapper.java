package ar.gym.gym.mapper;

import ar.gym.gym.dto.request.TrainingDiaryRequestDto;
import ar.gym.gym.dto.response.TrainingDiaryResponseDto;
import ar.gym.gym.model.TrainingDiary;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class TrainingDiaryMapper {
    private final ModelMapper modelMapper;


    public TrainingDiaryMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public TrainingDiaryResponseDto entityToDto(TrainingDiary trainingDiary){
        return modelMapper.map(trainingDiary, TrainingDiaryResponseDto.class);
    }

    public TrainingDiary dtoToEntity(TrainingDiaryRequestDto trainingDiaryRequestDto){
        return modelMapper.map(trainingDiaryRequestDto, TrainingDiary.class);
    }
}
