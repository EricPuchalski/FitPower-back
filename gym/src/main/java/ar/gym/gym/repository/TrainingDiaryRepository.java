package ar.gym.gym.repository;


import ar.gym.gym.model.TrainingDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingDiaryRepository extends JpaRepository<TrainingDiary, Long> {
}
