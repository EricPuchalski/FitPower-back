package ar.gym.gym.repository;

import ar.gym.gym.model.Client;
import ar.gym.gym.model.TrainingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingPlanRepository extends JpaRepository<TrainingPlan, Long> {
    Optional<TrainingPlan> findById(Long id);

    List<TrainingPlan> findByClientAndActiveTrue(Client client);

    Optional<TrainingPlan> findByClient(Client client);
}
