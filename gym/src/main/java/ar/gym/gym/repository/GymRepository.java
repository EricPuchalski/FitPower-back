package ar.gym.gym.repository;

import ar.gym.gym.model.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {
    Optional<Gym> findByName(String name);
    Optional<Gym> findByEmail(String email);
    Optional<Gym> findByPhone(String phone);

}
