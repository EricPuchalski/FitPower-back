package ar.gym.gym.repository;

import ar.gym.gym.model.Food;
import com.itec.FitFlowApp.model.entity.Foot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    Food findByName(String name);
}
