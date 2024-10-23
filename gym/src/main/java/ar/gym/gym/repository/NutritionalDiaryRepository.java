package ar.gym.gym.repository;

import ar.gym.gym.model.NutritionalDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutritionalDiaryRepository extends JpaRepository<NutritionalDiary, Long> {
}
