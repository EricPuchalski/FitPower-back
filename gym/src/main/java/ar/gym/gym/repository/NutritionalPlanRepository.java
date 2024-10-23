package ar.gym.gym.repository;

import ar.gym.gym.model.NutritionalPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutritionalPlanRepository extends JpaRepository<NutritionalPlan, Long> {
    NutritionalPlan findByNutCode(String nutCode);
}
