package ar.gym.gym.repository;


import ar.gym.gym.model.Client;
import ar.gym.gym.model.TrainingDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingDiaryRepository extends JpaRepository<TrainingDiary, Long> {

    List<TrainingDiary> findByClient(Client client);
}
