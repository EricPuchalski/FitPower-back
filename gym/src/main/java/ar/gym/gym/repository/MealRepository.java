package ar.gym.gym.repository;


import ar.gym.gym.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByCompleted(boolean b);
    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.mealDetails")
    List<Meal> findAllWithDetails();
}
