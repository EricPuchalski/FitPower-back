package ar.gym.gym.repository;

import ar.gym.gym.model.NutritionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NutritionPlanRepository extends JpaRepository<NutritionPlan, Long> {
    List<NutritionPlan> findByClientDni(String dniClient);

    List<NutritionPlan> findByActive(boolean active);

    List<NutritionPlan> findByCompleted(boolean b);

    List<NutritionPlan> findByClientId(Long clientId);

    List<NutritionPlan> findByNutritionistId(Long nutritionistId);
}
