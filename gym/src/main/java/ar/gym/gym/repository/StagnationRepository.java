package ar.gym.gym.repository;


import ar.gym.gym.model.Stagnation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StagnationRepository extends JpaRepository<Stagnation, Long> {
}
