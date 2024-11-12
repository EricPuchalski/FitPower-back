package ar.gym.gym.repository;

import ar.gym.gym.model.MealDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealItemRepository extends JpaRepository<MealDetail, Long> {
}
