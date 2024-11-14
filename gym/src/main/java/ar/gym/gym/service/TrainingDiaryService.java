package ar.gym.gym.service;

import ar.gym.gym.dto.request.SessionToTrainingDiaryRequestDto;
import ar.gym.gym.dto.request.TrainingDiaryRequestDto;
import ar.gym.gym.dto.response.SessionToTrainingDiaryResponseDto;
import ar.gym.gym.dto.response.TrainingDiaryResponseDto;

import java.util.List;

public interface TrainingDiaryService {
    TrainingDiaryResponseDto createTrainingDiary(TrainingDiaryRequestDto requestDto);
    List<TrainingDiaryResponseDto> getAllDiariesByClient(String clientDni);
    TrainingDiaryResponseDto getDiaryById(Long id);
    TrainingDiaryResponseDto updateTrainingDiary(Long id, TrainingDiaryRequestDto requestDto);
    void deleteTrainingDiary(Long id);

    SessionToTrainingDiaryResponseDto addSessionToDiary(Long trainingDiaryId, SessionToTrainingDiaryRequestDto sessionRequestDto);

    List<TrainingDiaryResponseDto> findAllTrainingDiaries();

    List<SessionToTrainingDiaryResponseDto> getSessionsByTrainingDiaryId(Long trainingDiaryId);

    void deleteSession(Long idSession);

}
