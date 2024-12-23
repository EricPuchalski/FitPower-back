package ar.gym.gym.repository;

import ar.gym.gym.model.Nutritionist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NutritionistRepository extends JpaRepository<Nutritionist, Long> {
    Optional<Nutritionist> findByDni(String dni);

}
