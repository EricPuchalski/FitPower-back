package ar.gym.gym.repository;

import ar.gym.gym.model.NutritionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutritionLogRepository extends JpaRepository<NutritionLog, Long> {
}
