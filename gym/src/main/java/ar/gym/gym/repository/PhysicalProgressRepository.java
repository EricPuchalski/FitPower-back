package ar.gym.gym.repository;

import ar.gym.gym.model.PhysicalProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhysicalProgressRepository extends JpaRepository<PhysicalProgress, Long> {
}
