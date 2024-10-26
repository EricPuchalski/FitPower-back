package ar.gym.gym.repository;

import ar.gym.gym.model.NutritionalPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NutritionalPlanRepository extends JpaRepository<NutritionalPlan, Long> {
    List<NutritionalPlan> findByClientDni(String dniClient);
    List<NutritionalPlan> findByActive(boolean active);
}
