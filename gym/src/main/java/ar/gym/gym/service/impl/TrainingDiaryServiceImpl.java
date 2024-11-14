package ar.gym.gym.service.impl;

import ar.gym.gym.dto.request.SessionToTrainingDiaryRequestDto;
import ar.gym.gym.dto.request.TrainingDiaryRequestDto;
import ar.gym.gym.dto.response.SessionToTrainingDiaryResponseDto;
import ar.gym.gym.dto.response.TrainingDiaryResponseDto;
import ar.gym.gym.mapper.SessionMapper;
import ar.gym.gym.mapper.TrainingDiaryMapper;
import ar.gym.gym.model.Client;
import ar.gym.gym.model.Exercise;
import ar.gym.gym.model.Session;
import ar.gym.gym.model.TrainingDiary;
import ar.gym.gym.repository.ClientRepository;
import ar.gym.gym.repository.ExerciseRepository;
import ar.gym.gym.repository.SessionRepository;
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
    private final SessionRepository sessionRepository;
    private final ExerciseRepository exerciseRepository;

    public TrainingDiaryServiceImpl(TrainingDiaryRepository trainingDiaryRepository, ClientRepository clientRepository, TrainingDiaryMapper trainingDiaryMapper, SessionMapper sessionMapper, SessionRepository sessionRepository, ExerciseRepository exerciseRepository) {
        this.trainingDiaryRepository = trainingDiaryRepository;
        this.clientRepository = clientRepository;
        this.trainingDiaryMapper = trainingDiaryMapper;
        this.sessionMapper = sessionMapper;
        this.sessionRepository = sessionRepository;
        this.exerciseRepository = exerciseRepository;
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
        trainingDiary.setCreationDate(LocalDateTime.now());

        // Guardar en la base de datos
        trainingDiary = trainingDiaryRepository.save(trainingDiary);

        // Convertir la entidad guardada a DTO de respuesta
        return trainingDiaryMapper.entityToDto(trainingDiary);
    }

    @Override
    public List<SessionToTrainingDiaryResponseDto> getSessionsByTrainingDiaryId(Long trainingDiaryId) {
        // Buscar el diario de entrenamiento por ID
        TrainingDiary trainingDiary = trainingDiaryRepository.findById(trainingDiaryId)
                .orElseThrow(() -> new EntityNotFoundException("Training Diary not found with ID: " + trainingDiaryId));

        // Obtener las sesiones del diario y mapearlas a DTOs
        return trainingDiary.getSessions()
                .stream()
                .map(sessionMapper::entityTrainingToDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<TrainingDiaryResponseDto> getAllDiariesByClient(String clientDni) {
        // Buscar todos los diarios de entrenamiento del cliente por DNI
        Client client = clientRepository.findByDni(clientDni)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with DNI: " + clientDni));

        return trainingDiaryRepository.findByClient(client)
                .stream()
                .map(trainingDiaryMapper::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TrainingDiaryResponseDto getDiaryById(Long id) {
        // Buscar el diario de entrenamiento por ID
        TrainingDiary trainingDiary = trainingDiaryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Training Diary not found with ID: " + id));

        return trainingDiaryMapper.entityToDto(trainingDiary);
    }

    @Override
    @Transactional
    public TrainingDiaryResponseDto updateTrainingDiary(Long id, TrainingDiaryRequestDto requestDto) {
        // Buscar el diario de entrenamiento existente por ID
        TrainingDiary trainingDiary = trainingDiaryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Training Diary not found with ID: " + id));

        // Actualizar los campos con los datos del requestDto
        trainingDiary.setObservation(requestDto.getObservation());

        // Guardar los cambios
        trainingDiary = trainingDiaryRepository.save(trainingDiary);

        return trainingDiaryMapper.entityToDto(trainingDiary);
    }

    @Override
    @Transactional
    public void deleteTrainingDiary(Long id) {
        // Verificar si el diario de entrenamiento existe antes de eliminar
        TrainingDiary trainingDiary = trainingDiaryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Training Diary not found with ID: " + id));

        trainingDiaryRepository.delete(trainingDiary);
    }


    @Override
    @Transactional
    public void deleteSession(Long idSession) {
        // Buscar la sesión por ID
        Session session = sessionRepository.findById(idSession)
                .orElseThrow(() -> new EntityNotFoundException("Session not found with ID: " + idSession));

        // Eliminar la sesión
        sessionRepository.delete(session);
    }

    @Override
    @Transactional
    public SessionToTrainingDiaryResponseDto addSessionToDiary(Long trainingDiaryId, SessionToTrainingDiaryRequestDto sessionRequestDto) {
        // Buscar el diario de entrenamiento por ID
        TrainingDiary trainingDiary = trainingDiaryRepository.findById(trainingDiaryId)
                .orElseThrow(() -> new EntityNotFoundException("TrainingDiary not found with ID: " + trainingDiaryId));

        // Buscar el ejercicio usando el nombre del DTO y el repositorio de ejercicios
        Exercise exercise = exerciseRepository.findByName(sessionRequestDto.getExerciseName())
                .orElseThrow(() -> new EntityNotFoundException("Exercise not found with name: " + sessionRequestDto.getExerciseName()));

        // Convertir el DTO de la sesión a entidad y asignar el ejercicio
        Session session = sessionMapper.dtoTrainingToEntity(sessionRequestDto);
        session.setExercise(exercise);  // Establecer el ejercicio en la sesión
        session.setWeight(sessionRequestDto.getWeight());
        session.setTrainingDiary(trainingDiary);  // Establecer la relación con el diario

        // Agregar la nueva sesión al diario de entrenamiento
        trainingDiary.getSessions().add(session);

        // Guardar la sesión (se guarda en cascada junto con el diario)
        trainingDiaryRepository.save(trainingDiary);

        // Convertir la sesión recién añadida a DTO de respuesta
        return sessionMapper.entityTrainingToDto(session);
    }


    @Override
    public List<TrainingDiaryResponseDto> findAllTrainingDiaries() {
        return trainingDiaryRepository.findAll()
                .stream()
                .map(trainingDiaryMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
