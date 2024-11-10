package ar.gym.gym.repository;

import ar.gym.gym.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    Optional<Food> findByName(String name);

    List<Food> findByNameContainingIgnoreCase(String name);

    List<Food> findByCategory(String category);

    List<Food> findByCompleted(boolean b);
}
