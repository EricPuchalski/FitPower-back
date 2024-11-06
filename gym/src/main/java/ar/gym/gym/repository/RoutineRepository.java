package ar.gym.gym.repository;

import ar.gym.gym.dto.response.RoutineResponseDto;
import ar.gym.gym.model.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findByClientIdOrderByCreationDateAsc(Long clientId);


    Optional<Routine> findFirstByClientIdAndActiveTrue(Long clientId);

    List<Routine> findByClientId(Long clientId);

    Optional<Routine> findByClientIdAndActiveTrue(Long id);
 }
