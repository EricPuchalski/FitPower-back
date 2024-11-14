package ar.gym.gym.repository;

import ar.gym.gym.model.Routine;
import ar.gym.gym.model.TrainingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {

    List<Routine> findByClientId(Long clientId);

    Optional<Routine> findByClientIdAndActiveTrue(Long id);

    List<Routine> findAllByClientDni(String clientDni);

    List<Routine> findByTrainingPlanAndActiveTrue(TrainingPlan trainingPlan);

}
