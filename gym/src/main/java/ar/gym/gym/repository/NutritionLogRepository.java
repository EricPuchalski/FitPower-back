package ar.gym.gym.repository;

import ar.gym.gym.model.NutritionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NutritionLogRepository extends JpaRepository<NutritionLog, Long> {
    List<NutritionLog> findByClientId(Long clientId);

    List<NutritionLog> findByNutritionPlanId(Long nutritionPlanId);

    List<NutritionLog> findByDate(LocalDateTime date);

    List<NutritionLog> findByCompleted(boolean b);

    @Query("SELECT nl FROM NutritionLog nl LEFT JOIN FETCH nl.meals")
    List<NutritionLog> findAllWithMeals();

}
