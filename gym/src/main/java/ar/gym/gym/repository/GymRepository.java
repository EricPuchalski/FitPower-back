package ar.gym.gym.repository;

import ar.gym.gym.dto.response.GymResponseDto;
import ar.gym.gym.model.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {
    Optional<Gym> findByGymCode(String GymCode);

    Optional<Gym> findByName(String name);

}
