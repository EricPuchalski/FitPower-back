package ar.gym.gym.controller;

import ar.gym.gym.dto.request.SessionToTrainingDiaryRequestDto;
import ar.gym.gym.dto.request.TrainingDiaryRequestDto;
import ar.gym.gym.dto.response.SessionToTrainingDiaryResponseDto;
import ar.gym.gym.dto.response.TrainingDiaryResponseDto;
import ar.gym.gym.service.TrainingDiaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<TrainingDiaryResponseDto> createTrainingDiary(@RequestBody TrainingDiaryRequestDto requestDto) {
        TrainingDiaryResponseDto createdDiary = trainingDiaryService.createTrainingDiary(requestDto);
        return ResponseEntity.ok(createdDiary);
    }

    @GetMapping("/client/{clientDni}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_TRAINER')")
    public ResponseEntity<List<TrainingDiaryResponseDto>> getAllDiariesByClient(@PathVariable String clientDni) {
        List<TrainingDiaryResponseDto> diaries = trainingDiaryService.getAllDiariesByClient(clientDni);
        return ResponseEntity.ok(diaries);
    }

    @GetMapping("/{id}/sessions")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_TRAINER')")
    public List<SessionToTrainingDiaryResponseDto> getSessionsByTrainingDiaryId(@PathVariable Long id) {
        return trainingDiaryService.getSessionsByTrainingDiaryId(id);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_TRAINER')")
    public ResponseEntity<TrainingDiaryResponseDto> getDiaryById(@PathVariable Long id) {
        TrainingDiaryResponseDto diary = trainingDiaryService.getDiaryById(id);
        return ResponseEntity.ok(diary);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<TrainingDiaryResponseDto> updateTrainingDiary(
            @PathVariable Long id,
            @RequestBody TrainingDiaryRequestDto requestDto) {
        TrainingDiaryResponseDto updatedDiary = trainingDiaryService.updateTrainingDiary(id, requestDto);
        return ResponseEntity.ok(updatedDiary);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT')")
    public ResponseEntity<Void> deleteTrainingDiary(@PathVariable Long id) {
        trainingDiaryService.deleteTrainingDiary(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/sessions")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<SessionToTrainingDiaryResponseDto> addSessionToDiary(
            @PathVariable Long id,
            @RequestBody SessionToTrainingDiaryRequestDto sessionRequestDto) {
        SessionToTrainingDiaryResponseDto newSession = trainingDiaryService.addSessionToDiary(id, sessionRequestDto);
        return ResponseEntity.ok(newSession);
    }



    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<TrainingDiaryResponseDto>> getAllTrainingDiaries() {
        List<TrainingDiaryResponseDto> diaries = trainingDiaryService.findAllTrainingDiaries();
        return ResponseEntity.ok(diaries);
    }

    // Endpoint para eliminar una sesión por ID
    @DeleteMapping("/sessions/{idSession}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT')")
    public ResponseEntity<Void> deleteSession(@PathVariable Long idSession) {
        trainingDiaryService.deleteSession(idSession);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
    }
}