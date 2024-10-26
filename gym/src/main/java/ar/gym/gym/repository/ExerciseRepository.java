package ar.gym.gym.repository;

import ar.gym.gym.dto.response.ExerciseResponseDto;
import ar.gym.gym.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Optional<Exercise> findByName(String name);

}
