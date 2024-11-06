package ar.gym.gym.service;

import ar.gym.gym.dto.request.SessionRequestDto;
import ar.gym.gym.dto.request.TrainingDiaryRequestDto;
import ar.gym.gym.dto.response.TrainingDiaryResponseDto;

import java.util.List;

public interface TrainingDiaryService {
    TrainingDiaryResponseDto createTrainingDiary(TrainingDiaryRequestDto requestDto);
    List<TrainingDiaryResponseDto> getAllDiariesByClient(String clientDni);
    TrainingDiaryResponseDto getDiaryById(Long id);
    TrainingDiaryResponseDto updateTrainingDiary(Long id, TrainingDiaryRequestDto requestDto);
    void deleteTrainingDiary(Long id);

    TrainingDiaryResponseDto addSessionToDiary(Long trainingDiaryId, SessionRequestDto sessionRequestDto);

    List<TrainingDiaryResponseDto> findAllTrainingDiaries();

}
