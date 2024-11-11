package ar.gym.gym.repository;

import ar.gym.gym.model.MealItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealItemRepository extends JpaRepository<MealItem, Long> {
}
