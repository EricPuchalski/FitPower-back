package ar.gym.gym.controller;

import ar.gym.gym.dto.request.SessionRequestDto;
import ar.gym.gym.dto.request.SessionToTrainingDiaryRequestDto;
import ar.gym.gym.dto.request.TrainingDiaryRequestDto;
import ar.gym.gym.dto.response.SessionToTrainingDiaryResponseDto;
import ar.gym.gym.dto.response.TrainingDiaryResponseDto;
import ar.gym.gym.service.TrainingDiaryService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training-diaries")
public class TrainingDiaryController {

    private final TrainingDiaryService trainingDiaryService;


    public TrainingDiaryController(TrainingDiaryService trainingDiaryService) {
        this.trainingDiaryService = trainingDiaryService;
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<TrainingDiaryResponseDto> createTrainingDiary(@RequestBody TrainingDiaryRequestDto requestDto) {
        TrainingDiaryResponseDto createdDiary = trainingDiaryService.createTrainingDiary(requestDto);
        return ResponseEntity.ok(createdDiary);
    }

    @GetMapping("/client/{clientDni}")
    public ResponseEntity<List<TrainingDiaryResponseDto>> getAllDiariesByClient(@PathVariable String clientDni) {
        List<TrainingDiaryResponseDto> diaries = trainingDiaryService.getAllDiariesByClient(clientDni);
        return ResponseEntity.ok(diaries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainingDiaryResponseDto> getDiaryById(@PathVariable Long id) {
        TrainingDiaryResponseDto diary = trainingDiaryService.getDiaryById(id);
        return ResponseEntity.ok(diary);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrainingDiaryResponseDto> updateTrainingDiary(
            @PathVariable Long id,
            @RequestBody TrainingDiaryRequestDto requestDto) {
        TrainingDiaryResponseDto updatedDiary = trainingDiaryService.updateTrainingDiary(id, requestDto);
        return ResponseEntity.ok(updatedDiary);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainingDiary(@PathVariable Long id) {
        trainingDiaryService.deleteTrainingDiary(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sessions")
    public ResponseEntity<SessionToTrainingDiaryResponseDto> addSessionToDiary(
            @PathVariable Long id,
            @RequestBody SessionToTrainingDiaryRequestDto sessionRequestDto) {
        SessionToTrainingDiaryResponseDto newSession = trainingDiaryService.addSessionToDiary(id, sessionRequestDto);
        return ResponseEntity.ok(newSession);
    }



    @GetMapping
    public ResponseEntity<List<TrainingDiaryResponseDto>> getAllTrainingDiaries() {
        List<TrainingDiaryResponseDto> diaries = trainingDiaryService.findAllTrainingDiaries();
        return ResponseEntity.ok(diaries);
    }
}