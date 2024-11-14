package ar.gym.gym.repository;

import ar.gym.gym.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByDni(String dni);

    Trainer findByEmail(String email);
}
