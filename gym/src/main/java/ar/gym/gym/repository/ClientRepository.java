package ar.gym.gym.repository;

import ar.gym.gym.model.Client;
import ar.gym.gym.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByDni(String dni);

    Optional<Client> findByPhone(String phoneNumber);

    Optional<Client> findByEmail(String email);
    @Query("SELECT n FROM Notification n WHERE n.client.dni = :dni AND n.seen = false")
    List<Notification> findByDniAndNotificationsSeenFalse(String dni);
}
