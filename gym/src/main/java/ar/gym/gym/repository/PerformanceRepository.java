package ar.gym.gym.repository;

import ar.gym.gym.model.Client;
import ar.gym.gym.model.Performance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PerformanceRepository extends JpaRepository<Performance, Long> {
    Optional<Performance> findByClientDni(String dni);
}
