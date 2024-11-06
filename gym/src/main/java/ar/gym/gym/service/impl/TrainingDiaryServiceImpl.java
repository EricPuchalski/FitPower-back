package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.SessionRequestDto;
import ar.gym.gym.dto.request.TrainingDiaryRequestDto;
import ar.gym.gym.dto.response.TrainingDiaryResponseDto;
import ar.gym.gym.mapper.SessionMapper;
import ar.gym.gym.mapper.TrainingDiaryMapper;
import ar.gym.gym.model.Client;
import ar.gym.gym.model.Session;
import ar.gym.gym.model.TrainingDiary;
import ar.gym.gym.repository.ClientRepository;
import ar.gym.gym.repository.TrainingDiaryRepository;
import ar.gym.gym.service.TrainingDiaryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingDiaryServiceImpl implements TrainingDiaryService {

    private final TrainingDiaryRepository trainingDiaryRepository;
    private final ClientRepository clientRepository;
    private final TrainingDiaryMapper trainingDiaryMapper;
    private final SessionMapper sessionMapper;

    public TrainingDiaryServiceImpl(TrainingDiaryRepository trainingDiaryRepository, ClientRepository clientRepository, TrainingDiaryMapper trainingDiaryMapper, SessionMapper sessionMapper) {
        this.trainingDiaryRepository = trainingDiaryRepository;
        this.clientRepository = clientRepository;
        this.trainingDiaryMapper = trainingDiaryMapper;
        this.sessionMapper = sessionMapper;
    }

    @Override
    @Transactional
    public TrainingDiaryResponseDto createTrainingDiary(TrainingDiaryRequestDto requestDto) {
        // Buscar el cliente por DNI
        Client client = clientRepository.findByDni(requestDto.getClientDni())
                .orElseThrow(() -> new EntityNotFoundException("Client not found with DNI: " + requestDto.getClientDni()));

        // Convertir el DTO a entidad
        TrainingDiary trainingDiary = trainingDiaryMapper.dtoToEntity(requestDto);
        trainingDiary.setClient(client);
        trainingDiary.setDate(LocalDateTime.now());

        // Guardar en la base de datos
        trainingDiary = trainingDiaryRepository.save(trainingDiary);

        // Convertir la entidad guardada a DTO de respuesta
        return trainingDiaryMapper.entityToDto(trainingDiary);
    }

    @Override
    public List<TrainingDiaryResponseDto> getAllDiariesByClient(String clientDni) {
        // Buscar todos los diarios de entrenamiento del cliente por DNI
        Client client = clientRepository.findByDni(clientDni)
                .orElseThrow(() -> new RuntimeException("Client not found with DNI: " + clientDni));

        return trainingDiaryRepository.findByClient(client)
                .stream()
                .map(trainingDiaryMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TrainingDiaryResponseDto getDiaryById(Long id) {
        // Buscar el diario de entrenamiento por ID
        TrainingDiary trainingDiary = trainingDiaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Training Diary not found with ID: " + id));

        return trainingDiaryMapper.entityToDto(trainingDiary);
    }

    @Override
    @Transactional
    public TrainingDiaryResponseDto updateTrainingDiary(Long id, TrainingDiaryRequestDto requestDto) {
        // Buscar el diario de entrenamiento existente por ID
        TrainingDiary trainingDiary = trainingDiaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Training Diary not found with ID: " + id));

        // Actualizar los campos con los datos del requestDto
        trainingDiary.setObservation(requestDto.getObservation());
        trainingDiary.setCompleted(requestDto.getSession() != null);

        // Guardar los cambios
        trainingDiary = trainingDiaryRepository.save(trainingDiary);

        return trainingDiaryMapper.entityToDto(trainingDiary);
    }

    @Override
    @Transactional
    public void deleteTrainingDiary(Long id) {
        // Verificar si el diario de entrenamiento existe antes de eliminar
        TrainingDiary trainingDiary = trainingDiaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Training Diary not found with ID: " + id));

        trainingDiaryRepository.delete(trainingDiary);
    }


    @Override
    @Transactional
    public TrainingDiaryResponseDto addSessionToDiary(Long trainingDiaryId, SessionRequestDto sessionRequestDto) {
        // Buscar el diario de entrenamiento por ID
        TrainingDiary trainingDiary = trainingDiaryRepository.findById(trainingDiaryId)
                .orElseThrow(() -> new RuntimeException("TrainingDiary not found with ID: " + trainingDiaryId));

        // Convertir el DTO de la sesión a entidad
        Session session = sessionMapper.dtoToEntity(sessionRequestDto);
        session.setTrainingDiary(trainingDiary);  // Establecer la relación

        // Agregar la nueva sesión al diario de entrenamiento
        trainingDiary.getSessions().add(session);

        // Guardar el diario actualizado (cascade persistencia guarda las sesiones)
        TrainingDiary updatedDiary = trainingDiaryRepository.save(trainingDiary);

        // Convertir el resultado a DTO de respuesta y retornarlo
        return trainingDiaryMapper.entityToDto(updatedDiary);
    }

    @Override
    public List<TrainingDiaryResponseDto> findAllTrainingDiaries() {
        return trainingDiaryRepository.findAll()
                .stream()
                .map(trainingDiaryMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
