package ar.gym.gym.repository;

import ar.gym.gym.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByDni(String dni);

    Optional<Client> findByPhone(String phoneNumber);

    Optional<Client> findByEmail(String email);
}
